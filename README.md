# eSignatureServer

[![Build Status](https://travis-ci.org/Giotino/eSignatureServer.svg?branch=master)](https://travis-ci.org/Giotino/eSignatureServer)

A simple REST server to sign and validate documents following eIDAS specifications (currently only PAdES format)

## Config

Simple `config.json` (See `config.full.json` for a full configuration with comments)

```json
{
  "pkcs11Provider": {
    "driverPath": "driver.dll",
    "pin": "123456",
    "keyName": "DS0"
  }
}
```

## API

All requests with file must be `multipart/form-data`

#### /sign/immediate

Sign the file as soon as possible and send it as response

| parameter | type   | description                 |
|-----------|--------|-----------------------------|
| file      | file   | The file to sign            |
| format    | string | The format of the signature |

#### /sign/deferred

Add the file the deferred sign queue, once signed the file will be posted to `webhook`

| parameter | type        | description                 |
|-----------|-------------|-----------------------------|
| file      | file        | The file to sign            |
| format    | string      | The format of the signature |
| webhook   | string(url) | The webhook                 |

#### Webhook specs for /sign/deferred

The request is a `multipart/form-data` as described below

| parameter | type    | description                      |
|-----------|---------|----------------------------------|
| status    | boolean | The status of the signature      |
| [error]   | string  | The error (if `status` is false) |
| [file]    | file    | The file (if `status` is true)   |

#### /verify/isSignedBy

Verify if a file is signed by `fiscal_code`

| parameter   | type   | description                 |
|-------------|--------|-----------------------------|
| file        | file   | The file to sign            |
| format      | string | The format of the signature |
| fiscal_code | string | The fiscal code to validate |
