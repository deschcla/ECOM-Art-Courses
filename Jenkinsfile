pipeline {
    agent any

    tools {
        // Install the Maven version configured as "Default" and add it to the path.
        maven "Default"
        nodejs "Default"
    }

    stages {
        stage('Build frontend'){
            steps{
                sh "npm install"
            }
        }
        stage('check java') {
            steps{
                sh "java -version"
            }

        }
        stage('clean') {
            steps{
                sh "chmod +x mvnw"
                sh "./mvnw -ntp clean -P-webapp"
            }
        }
        stage('nohttp') {
            steps{
                sh "./mvnw -ntp checkstyle:check"

            }
        }
//         stage('backend tests') {
//             try {
//                 sh "./mvnw -ntp verify -P-webapp"
//             } catch(err) {
//                 throw err
//             } finally {
//                 junit '**/target/surefire-reports/TEST-*.xml,**/target/failsafe-reports/TEST-*.xml'
//             }
//         }
//
//         stage('frontend tests') {
//             try {
//                sh "npm install"
//                sh "npm test"
//             } catch(err) {
//                 throw err
//             } finally {
//                 junit '**/target/test-results/TESTS-results-jest.xml'
//             }
//         }
        stage('packaging') {
            steps{
                sh "./mvnw -ntp verify -P-webapp -Pprod -DskipTests"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true

            }
        }
    }
}
