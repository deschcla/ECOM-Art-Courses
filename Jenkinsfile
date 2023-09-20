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
        stage('DockerHub setup'){
            withCredentials([usernamePassword(credentialsId: 'dockerhubCredentials', usernameVariable: 'DOCKERHUB_USR', passwordVariable: 'DOCKERHUB_PSW')]) {
                script {
                    sh '''
                        echo $DOCKERHUB_USR
                        echo "DOCKERHUB_USERNAME=${DOCKERHUB_USR}" > .env
                        echo "DOCKERHUB_PASSWORD=${DOCKERHUB_PSW}" >> .env
                    '''
                }
            }
        }
        stage('install tools') {
            sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm@install-node-and-npm"
        }
        stage('npm install') {
            sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"
        }
        stage('backend test') {
            try {
                sh "./mvnw -ntp clean test -P-webapp"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml,**/target/failsafe-reports/TEST-*.xml'
            }
        }
        stage('frontend build') {
            sh "npm install"
            sh "npm run build"
        }
        stage('Deploy to dockerhub') {
            sh "./mvnw -Pprod clean package verify -DskipTests jib:build -Djib.to.image=rocdaana27/ecom-art-courses"
        }

    }
}
