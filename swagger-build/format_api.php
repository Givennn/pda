<?php header('Content-type: text/plain; charset=utf-8');

$content  = json_decode(file_get_contents('./swagger.json'), true);
//$content = $yaml->parse(file_get_contents('./swagger.yaml'));

$paths = $content['paths'];
global $definitions;
$definitions = $content['definitions'];

unset($content['tags']);
unset($content['errorCodes']);
unset($content['commonParams']);
unset($content['apiEnvs']);
foreach ($paths as $key => $path) {
    // /web/
    if (strpos($key, '/web/') !== false || startsWith($key, '/app/admin/role/') !== true) {
        // printf("remove path $key\n");
        unset($paths[$key]);
    } else {
        $paths[$key] = handlePath($key, $path);
    }
}

$content['paths'] = $paths;
$content['definitions'] = $definitions;

file_put_contents('./docs.json', str_replace("\\/", "/", json_encode($content, JSON_UNESCAPED_UNICODE)));

function handlePath($url, $path) {
    foreach ($path as $key => $request) {
        $path[$key] = handleRequest( $url ,$key, $request);
    }

    return $path;
}

function handleRequest($path, $method, $request) {
    $request['operationId'] = basename($path, '.do').'Using'.strtoupper($method);
    unset($request["sortWeight"]);
    unset($request["devStatus"]);
    unset($request["devStatusName"]);
    unset($request["showDevStatus"]);
    unset($request["skipCommonParam"]);
    unset($request["label"]);
    unset($request["developer"]);
    unset($request["modifyDate"]);
    unset($request["description"]);
    unset($request["schemes"]);
    unset($request["consumes"]);
    unset($request["produces"]);

    if ($request['parameters']) {
        $parameters = [];
        foreach ($request['parameters'] as $key => $parameter) {
            $parameter = handleParameter($parameter);
            unset($request['parameters'][$key]);
            if ($parameter) {
                $parameters[] = $parameter;
            }
        }

        if (empty($parameters)) {
            unset($request['parameters']);
        } else {
            $request['parameters'] = $parameters;
        }
    }

    if ($request['responses']) {
        $responses = [];
        foreach ($request['responses'] as $key => $response) {
            $response = handleResponse($path, $method, $response);
            if ($response) {
                $responses[$key] = $response;
            }
        }
        if (empty($responses)) {
            unset($request['responses']);
        } else {
            $request['responses'] = $responses;
        }
    }
    
    return $request;
}

function handleResponse($path, $method, $response) {
    if (isset($response['schema']) && isset($response['schema']['type'])  && $response['schema']['type'] == 'object' && isset($response['schema']['properties']) ) {
        $paths = explode("/",$path); 
        $name = "";
        foreach ($paths as $item) {
            $name .= ucfirst($item);
        }
        $name.=ucfirst($method);
        $name .= "Response";
        $definition = removeRequiredField($response['schema']);
        if(isset($definition['properties']['data']['properties'])) {
            $definition = $definition['properties']['data'];
            global $definitions; 
            $definitions[$name] = $definition;
            $response['schema']= ['$ref'=> "#/definitions/".$name];
        }
        
    }    
    unset($response["sortWeight"]);
    return $response;
}

function removeRequiredField($node) {
    $response = $node;
    if (isset($response['required'])) {
        unset($response['required']);
    }
   
    if (isset($response['properties'])) {
        foreach ($response['properties'] as $key => $property) {
            $response['properties'][$key] = removeRequiredField($property);
        }
    }
    if (isset($response['items'])) {
        $response['items'] = removeRequiredField($response['items']);
    }
    printf(json_encode($response, JSON_UNESCAPED_UNICODE));

    return $response;
}

function handleParameter($parameter) {
    $value = array_values($parameter);
    if(isset($parameter['$ref']) ) {
        $ref = $parameter['$ref'];
        if ($ref == '#/parameters/AppKey' || $ref == '#/parameters/Timestamp' || $ref== '#/parameters/Signature' || $ref == '#/parameters/AccessToken') {
            return null;
        }
    }

    if( isset($parameter['name']) && $parameter['name'] == 'access_token') {
        return null;
    }

    return $parameter;
}

function startsWith($haystack, $needle) {
    // search backwards starting from haystack length characters from the end
    return $needle === "" || strrpos($haystack, $needle, -strlen($haystack)) !== false;
}