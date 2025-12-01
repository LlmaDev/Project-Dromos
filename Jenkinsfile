pipeline {
    agent any

    environment {
        WTO_API_KEY = credentials('wto-api-key-secret')
    }

    stages {
        stage('Check Environment') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'java -version'
                    } else {
                        bat 'java -version'
                    }
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Unit Tests') {
            steps {
                dir('backend') {
                    script {
                        if (isUnix()) {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean test -B'
                        } else {
                            bat 'mvnw.cmd clean test -B'
                        }
                    }
                }
            }
            post {
                always {
                    junit testResults: 'backend/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Build JAR') {
            steps {
                dir('backend') {
                    script {
                        if (isUnix()) {
                            sh './mvnw package -DskipTests -B'
                        } else {
                            bat 'mvnw.cmd package -DskipTests -B'
                        }
                    }
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'backend/target/maven-status/**/*', allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo '✅ Pipeline finalizado com sucesso!'
        }
        failure {
            echo '❌ Falha no Pipeline.'
        }
    }
}