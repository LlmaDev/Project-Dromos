pipeline {
    agent any
    
    stages {
        stage('Hello') {
            steps {
                echo 'Hello from Jenkins!'
                echo 'Pipeline is working!'
            }
        }
        
        stage('Check Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
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
                    // Executa o build com Maven
                    sh 'mvn clean package'
                }
            }
        }
    }
}
