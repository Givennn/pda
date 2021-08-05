#!/bin/sh

#wget --no-check-certificate -O ./swagger.json https://xxx.xxx.xxx/xxx/api-docs
php ./format_api.php
java -jar ./swagger-codegen-cli.jar generate -i ./docs.json -l java -c ./config.json -o ../api-client