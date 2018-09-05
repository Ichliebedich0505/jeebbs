#!/usr/bin/env bash
set -xe

M2_HOME=${M2_HOME-~/.m2}
if [ ! -d "$M2_HOME" ]; then
    mkdir $M2_HOME
fi

read -s -p "Enter mvn encrypt-master-password: " MVN_MASTER_PASSWORD
ENC_MVN_MASTER_PASSWORD=$(./mvnw --encrypt-master-password $MVN_MASTER_PASSWORD | tail -n +2)
echo "ENC_MVN_MASTER_PASSWORD: $ENC_MVN_MASTER_PASSWORD"
sed -i "s/examplepwd/$ENC_MVN_MASTER_PASSWORD/g" ./.settings-security.xml && mv ./.settings-security.xml ~/.m2/settings-security.xml

read -p "Please input docker hub username: " DOCKERHUB_USERNAME
export DOCKERHUB_USERNAME
read -s -p "Please input password for $DOCKERHUB_USERNAME: " DOCKERHUB_PASSWORD
export DOCKERHUB_PASSWORD=$(./mvnw --encrypt-password $DOCKERHUB_PASSWORD | tail -n +2)

./mvnw clean deploy -Dmaven.test.skip=true -s ./.travis-settings.xml -P stage