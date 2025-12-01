pipeline {
    agent any
    
    stages {
        
        stage('Check Environment') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'java -version'
                        sh 'mvn -version'
                    } else {
                        bat 'java -version'
                        bat 'mvn -version'
                    }
                }
            }
        }

        stage('Checkout') {
            steps {
                // Pega o código do repositório
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                dir('backend') {
                    script {
                        if (isUnix()) {
                            // Linux/Unix/Mac
                            sh 'mvn clean package'
                        } else {
                            // Windows
                            bat 'mvn clean package'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            // Limpa o workspace
            cleanWs()
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}