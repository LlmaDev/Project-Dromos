/*
 * PRÉ-REQUISITOS DO SERVIDOR JENKINS (MODO SEM DOCKER):
 * 1. Java (JDK 17 ou superior) deve estar instalado na máquina do Jenkins e acessível no PATH.
 * 2. Plugin "JaCoCo" instalado no Jenkins (para ler os relatórios de cobertura).
 */

pipeline {

    agent any

    stages {
        stage('Check Environment') {
            steps {
                sh 'java -version'
            }
        }

        stage('Test & Coverage') {
            steps {
                dir('backend') {
                    sh 'chmod +x mvnw'

                    echo 'Rodando bateria de testes...'
                    sh './mvnw clean test'
                }
            }
        }

        stage('Build Artifact') {
            steps {
                dir('backend') {
                    echo 'Gerando o arquivo .jar...'
                    sh './mvnw package -DskipTests'
                }
            }
        }
    }

    post {
        always {
            junit 'backend/target/surefire-reports/*.xml'

            // Se não tiver o plugin JaCoCo, essa linha vai dar erro. Se der, comente ela.
            jacoco(
                execPattern: 'backend/target/jacoco.exec',
                classPattern: 'backend/target/classes',
                sourcePattern: 'backend/src/main/java',
                inclusionPattern: '**/*.class'
            )
        }
        success {
            echo 'Pipeline finalizado com sucesso! Código testado e buildado.'
        }
        failure {
            echo 'Ops! Algum teste falhou ou o build quebrou.'
        }
    }
}