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
    // strpos($key, '/web/') !== false || 
    // 过滤接口
    // if (startsWith($key, '/pda/') !== true) {
        // unset($paths[$key]);
    // }
    // } else {
    //     $paths[$key] = handlePath($key, $path);
    // }
    if (startsWith($key, '/pda/qms/') !== true) {
        printf("remove path: $key\n");
        unset($paths[$key]);
    } else {
        $paths[$key] = handlePath($key, $path);
    }
}

$content['paths'] = $paths;
$content['definitions'] = $definitions;

file_put_contents('./docs.json', str_replace("\\/", "/", json_encode($content, JSON_UNESCAPED_UNICODE)));

printf("generate done.\n");

function handlePath($url, $path) {
    foreach ($path as $key => $request) {
        $path[$key] = handleRequest( $url ,$key, $request);
    }

    return $path;
}

function handleRequest($path, $method, $request) {
    // $request['operationId'] = basename($path, '.do').'Using'.strtoupper($method);
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
        
        // if (empty($parameters)) {
        //     unset($request['parameters']);
        // } else {
        //     if ($method == 'post') {
        //         $bodyParameter = handleBodyParameter($path, $parameters);
        //         printf("$path\n");
        //         printf(json_encode($parameters, JSON_UNESCAPED_UNICODE));
        //         printf("\n");
        //         if($bodyParameter) {
        //             $parameters[] = [];
        //             $parameters[] = $bodyParameter;
        //         }
        //         // var_dump($parameters);
        //     }
        // }
        
        $request['parameters'] = $parameters;
    }

    if ($request['responses']) {
        $responses = [];
        foreach ($request['responses'] as $key => $response) {
            $response = handleResponse($path, $method, $response);
            if ($response) {
                if($key == "20000") {
                    $key = "200";
                }
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

function handleBodyParameter($path, $parameterBodies) {

    printf("\n");
    if(empty($parameterBodies) !== true) {
        $paths = explode("/",$path); 
        $name = "";
        foreach ($paths as $item) {
            $name .= ucfirst($item);
        }
        $name.=ucfirst($method);
        $name .= "Request";
        $schema = [];
        $formattedParameterBody['in'] = "body";
        $formattedParameterBody['name'] = $name;
        $schema['type'] = "object";
        $properties = [];
        foreach ($parameterBodies as $key => $bodyParameter) {
            // var_dump($bodyParameter);
            $item = [];
            $item['type'] = $bodyParameter['type'];
            if($item['type'] == "cust") {
                $item['type'] = "string";
            }
            $item['description'] = $bodyParameter['description'];
            if(isset($bodyParameter['format'])) {
                $item['format'] = $bodyParameter['format'];
            }
            $propertyName = $bodyParameter['name'];
            printf("$propertyName\n");
            $properties[$propertyName] = $item; 
        }
        $schema['properties'] = $properties;
        $formattedParameterBody['schema'] = $schema;
        // var_dump($formattedParameterBody);
        return $formattedParameterBody;
    }
}

function handleResponse($path, $method, $response) {
    if (isset($response['schema']) && isset($response['schema']['type'])  && $response['schema']['type'] == 'object' && isset($response['schema']['properties']) ) {
        
        // $paths = explode("/",str_replace("/pda/", "", $path)); 
        $paths = explode("/", $path); 
        $name = "";
        foreach ($paths as $item) {
            if(strpos($item, '-') !== false) {
                $dashNames = explode("-",$item);
                $subName = "";
                foreach ($dashNames as $subItem) {
                    $subName .= ucfirst($subItem);
                }
                $name .= $subName;
            } else {
                $name .= ucfirst($item);
            }
        }
        $name.=ucfirst($method);
        $name .= "Response";
        $definition = removeRequiredField($response['schema']);
        if(isset($definition['properties']['data']['properties']) && empty($definition['properties']['data']['properties']) !== true) {
            $definition = $definition['properties']['data'];
            $definition['title'] = $name;
            global $definitions; 
            $definitions[$name] = $definition;
            $response['schema']= ['$ref'=> "#/definitions/".$name];
        } else {
            unset($response['schema']);
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