pipeline {
    agent any
    tools {
        maven 'Maven' // The name configured in the Global Tool Configuration
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // DockerHub credentials in Jenkins
        IMAGE_NAME = 'abhi7422/student_microservice'           // Docker image name
    }
    stages {
        
         stage('Build') {
            steps {
                echo 'Cleaning and packaging the application...'
                dir('/var/lib/jenkins/workspace/swe645hw3/studentSurveyMicroservice')
                    {
                      sh 'mvn clean install -DskipTests' // Use Maven for build
                
                     sh 'docker build -t $IMAGE_NAME:latest .' // Build Docker image
                    }
            }
        }
        
        stage('Login to Docker Hub') {
            steps {
                echo 'Logging in to DockerHub...'
                script {
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                }
            }
        }
        
        stage('Push Image to Docker Hub') {
            steps {
                echo 'Pushing the Docker image to DockerHub...'
                script {
                    // Push the Docker image to DockerHub
                    sh 'docker push abhi7422/student_microservice:latest'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying the application to Kubernetes...'
                script {
                    // Update the deployment image and monitor the rollout
                    sh 'kubectl set image deployment/swe645hw3deploy container-0=abhi7422/student_microservice:latest -n default'
                    sh 'kubectl rollout status deployment/swe645hw3deploy -n default'
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up DockerHub login...'
            script {
                // Logout from DockerHub after the pipeline completes
                sh 'docker logout'
            }
        }
    }
}
