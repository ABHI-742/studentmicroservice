pipeline {
    agent any
    tools {
        mvn 'Maven' // Replace 'Maven' with the name you configured
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // DockerHub credentials in Jenkins
        IMAGE_NAME = 'abhi7422/student_microservice'           // Docker image name
    }
    stages {
        stage('Build') {
            steps {
                echo 'Cleaning old builds and packaging the application...'
                sh 'mvn clean install -DskipTests'      // Build Spring Boot JAR
                sh 'docker build -t $IMAGE_NAME:latest .'  // Build Docker image
            }
        }

        stage('Login to Docker Hub') {
            steps {
                echo 'Logging in to DockerHub...'
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                echo 'Pushing the Docker image to DockerHub...'
                sh 'docker push $IMAGE_NAME:latest'
            }
        }

        stage('Deploy to Kubernetees') {
            steps {
                echo 'Deploying the application to Kubernetes...'
                sh 'kubectl set image deployment/swe645hw3deploy container-0=$IMAGE_NAME:latest -n default'
                sh 'kubectl rollout status deployment/swe645hw3deploy -n default'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up DockerHub login...'
            sh 'docker logout'
        }
    }
}
