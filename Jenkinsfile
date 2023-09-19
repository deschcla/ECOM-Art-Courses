#!/usr/bin/env groovy
node {
    stage('checkout') {
        checkout scm
    }

    docker.image('jhipster/jhipster:v8.0.0-beta.2').inside('-u jhipster -e MAVEN_OPTS="-Duser.home=./"') {
        stage('check java') {
            sh "java -version"
        }
        stage('clean') {
            sh "chmod +x mvnw"
            sh "./mvnw -ntp clean -P-webapp"
        }
        stage('nohttp') {
            sh "./mvnw -ntp checkstyle:check"
        }

        stage('install tools') {
            sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm@install-node-and-npm"
        }

        stage('npm install') {
            sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"
        }

        stage('backend test') {
            try {
                //sh "./mvnw -ntp verify -P-webapp"
                sh "./mvnw clean test"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml,**/target/failsafe-reports/TEST-*.xml'
            }
        }
//         stage('frontend build') {
//             try {
//                sh "npm install"
//                sh "npm test"
//             } catch(err) {
//                 throw err
//             } finally {
//                 junit '**/target/test-results/TESTS-results-jest.xml'
//             }
//         }

//         stage('packaging') {
//             sh "./mvnw -ntp verify -P-webapp -Pprod -DskipTests"
//             archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
//         }
        stage('DockerHub setup'){
            withCredentials([usernamePassword(credentialsId: '6aa2882d-fb9f-4995-985e-5e737302ca68', usernameVariable: 'DOCKERHUB_USR', passwordVariable: 'DOCKERHUB_PSW')]) {
                script {
                    sh '''
                        echo $DOCKERHUB_USR
                        echo "DOCKERHUB_USERNAME=${DOCKERHUB_USR}" > .env
                        echo "DOCKERHUB_PASSWORD=${DOCKERHUB_PSW}" >> .env
                    '''
                }
            }
        }

        stage('Deploy to dockerhub') {
            sh "./mvnw -Pprod clean package -DskipTests jib:build -Djib.to.image=rocdaana27/ecom-art-courses"

        }

    }
}
