[![java-bigchaindb-driver](media/repo-banner@2x.png)](https://www.bigchaindb.com)

> Official Java driver for [BigchainDB](https://github.com/bigchaindb/bigchaindb) created by [Authenteq](https://authenteq.com).

**Please note**: due to non-official org.json implementation the driver is not compatible with Android

## Compatibility

| BigchainDB Server | BigchainDB Java Driver |
| ----------------- |------------------------------|
| `1.0`             | `0.1.x`                      |


## Contents

* [Installation and Usage](#installation-and-usage)
   * [Example: Create a transaction](#example-create-a-transaction)
* [Documentation](#bigchaindb-documentation)
* [Authors](#authors)
* [License](#license)

## Installation and Usage
The build system now is fully gradle-based, so to build the driver run:
```bash
./gradlew install
```
### Example: Create and sign a transaction
```java
// Payload of the transaction, defined as the asset to store
JSONObject data = new JSONObject();
data.put("firstname", "John");
data.put("lastname", "Smith");

// Metadata contains information about the transaction itself
//                     (can be `null` if not needed)
JSONObject metadata = new JSONObject();
metadata.put("what", "My first BigchainDB transaction");

KeyPair keyPair = retrieveKeyPair();

BigchaindbTransaction bigchaindbTransaction = new BigchaindbTransaction(
        data, metadata, (EdDSAPublicKey) keyPair.getPublic()
);
bigchaindbTransaction.signTransaction((EdDSAPrivateKey) keyPair.getPrivate());
```

### Example: Send a transaction
```java
// creating connection (assuming the server is running on localhost)
final BigchaindbConnection bigchaindbConnection = new BigchaindbConnection("http://localhost:9984");

bigchaindbConnection.send(bigchaindbTransaction, new TransactionCallback() {
            @Override
            public void pushedSuccessfully() {
               // get the newly submitted transaction from remote
               BigchaindbTransaction tra =
                bigchaindbConnection.getTransactionById(bigchaindbTransaction.getTransactionId());
               System.out.println(tra.toString());
            }

            @Override
            public void transactionMalformed() {
              // transaction is malformed and has been rejected
            }

            @Override
            public void otherError() {
              // other unexpected server error
            }
});

```

## BigchainDB Documentation

- [HTTP API Reference](https://docs.bigchaindb.com/projects/server/en/latest/http-client-server-api.html)
- [The Transaction Model](https://docs.bigchaindb.com/projects/server/en/latest/data-models/transaction-model.html?highlight=crypto%20conditions)
- [Inputs and Outputs](https://docs.bigchaindb.com/projects/server/en/latest/data-models/inputs-outputs.html)
- [Asset Transfer](https://docs.bigchaindb.com/projects/py-driver/en/latest/usage.html#asset-transfer)
- [All BigchainDB Documentation](https://docs.bigchaindb.com/)

## Authors

- The [Authenteq](https://authenteq.com) team.

## License

```
Copyright 2017 Authenteq GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
