#!/bin/bash

# curl -Os https://uploader.codecov.io/latest/linux/codecov
# chmod +x codecov

source envvars.sh

clear
cd ..
./gradlew clean

##########################

./gradlew :app:jacocoFdroidAndroidTestReport
bin/codecov -t "$CODECOV_TOKEN" -F app-androidTest -f ./app/build/reports/jacoco/androidTest.xml

./gradlew :module:data-common:jacocoFdroidAndroidTestReport
bin/codecov -t "$CODECOV_TOKEN" -F data-common-androidTest -f ./module/data-common/build/reports/jacoco/androidTest.xml

./gradlew :module:ui:jacocoFdroidAndroidTestReport
bin/codecov -t "$CODECOV_TOKEN" -F ui-androidTest -f ./module/ui/build/reports/jacoco/androidTest.xml

##########################

./gradlew :module:ui:jacocoFdroidTestReport
bin/codecov -t "$CODECOV_TOKEN" -F ui-unitTest -f ./module/ui/build/reports/jacoco/unitTest.xml

./gradlew :module:data-repo-api:jacocoFdroidTestReport
bin/codecov -t "$CODECOV_TOKEN" -F data-repo-api-unitTest -f ./module/data-repo-api/build/reports/jacoco/unitTest.xml

./gradlew :module:data-common:jacocoFdroidTestReport
bin/codecov -t "$CODECOV_TOKEN" -F data-common-unitTest -f ./module/data-common/build/reports/jacoco/unitTest.xml
