# The various SSL stores and certificates were created with the following commands:
# This can be run as a script by sourcing the file, e.g ". store-generation.txt"
# Requires use of JDK 8+ keytool command.
set -e

KEY_PASS=securepass
STORE_PASS=securepass
CA_VALIDITY=365000
VALIDITY=36500
SERVER_NAMES="san=dns:localhost"

# Clean up existing files
# -----------------------
rm -f *.crt *.csr *.p12

# Create a key and self-signed certificate for the CA, to sign server certificate requests and use for trust:
# ----------------------------------------------------------------------------------------------------
keytool -storetype pkcs12 -keystore server-ca-keystore.p12 -storepass $STORE_PASS -keypass $KEY_PASS -alias server-ca -genkey -keyalg "RSA" -keysize 2048 -dname "CN=ActiveMQ Artemis Server Certification Authority, OU=Artemis, O=ActiveMQ" -validity $CA_VALIDITY -ext bc:c=ca:true
keytool -storetype pkcs12 -keystore server-ca-keystore.p12 -storepass $STORE_PASS -alias server-ca -exportcert -rfc > server-ca.crt

# Create trust store with the server CA cert:
# -------------------------------------------------------
keytool -storetype pkcs12 -keystore server-ca-truststore.p12 -storepass $STORE_PASS -keypass $KEY_PASS -importcert -alias server-ca -file server-ca.crt -noprompt

# Create a key pair for the server, and sign it with the CA:
# ----------------------------------------------------------
keytool -storetype pkcs12 -keystore server-keystore.p12 -storepass $STORE_PASS -keypass $KEY_PASS -alias server -genkey -keyalg "RSA" -keysize 2048 -dname "CN=ActiveMQ Artemis Server, OU=Artemis, O=ActiveMQ, L=AMQ, S=AMQ, C=AMQ" -validity $VALIDITY -ext bc=ca:false -ext eku=sA -ext $SERVER_NAMES

keytool -storetype pkcs12 -keystore server-keystore.p12 -storepass $STORE_PASS -alias server -certreq -file server.csr
keytool -storetype pkcs12 -keystore server-ca-keystore.p12 -storepass $STORE_PASS -alias server-ca -gencert -rfc -infile server.csr -outfile server.crt -validity $VALIDITY -ext bc=ca:false -ext $SERVER_NAMES

keytool -storetype pkcs12 -keystore server-keystore.p12 -storepass $STORE_PASS -keypass $KEY_PASS -importcert -alias server-ca -file server-ca.crt -noprompt
keytool -storetype pkcs12 -keystore server-keystore.p12 -storepass $STORE_PASS -keypass $KEY_PASS -importcert -alias server -file server.crt

# Clean up tmp files
rm -f *.crt *.csr

