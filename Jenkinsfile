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
            post {
                always {
                    // Arquiva os artifacts da build
                    archiveArtifacts artifacts: 'backend/target/*.jar', 
                                   fingerprint: true,
                                   allowEmptyArchive: true
                    
                    // Arquiva relatórios de teste
                    publishTestResults testResultsPattern: 'backend/target/surefire-reports/*.xml',
                                     allowEmptyResults: true
                    
                    // Arquiva logs da build
                    archiveArtifacts artifacts: 'backend/target/maven-status/**/*', 
                                   fingerprint: false,
                                   allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            // Limpa o workspace (mantém artifacts arquivados)
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