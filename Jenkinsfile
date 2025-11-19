pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = "rakshith01"
        AWS_REGION = "us-east-1"
        EKS_CLUSTER = "my-eks-cluster"
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/rakshi1993/demo_kubernets.git'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                withCredentials([string(credentialsId: 'docker-cerds', variable: 'DOCKER_HUB_PASS')]) {
                    sh """
                        echo "$DOCKER_HUB_PASS" | docker login -u ${DOCKER_HUB_USER} --password-stdin
                        docker build -t ${DOCKER_HUB_USER}/spring-success-app:latest .
                        docker push ${DOCKER_HUB_USER}/spring-success-app:latest
                    """
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                  credentialsId: 'aws_credentials']]) {

                    sh """
                        aws eks --region ${AWS_REGION} update-kubeconfig --name ${EKS_CLUSTER}
                        kubectl apply -f k8s/
                    """
                }
            }
        }

        stage('Get LoadBalancer URL') {
            steps {
                script {
                    sh '''
                        echo "⏳ Waiting for LoadBalancer external URL..."

                        # Try for up to 5 minutes (30 × 10s)
                        for i in {1..30}; do
                            LB_URL=$(kubectl get svc spring-service -o jsonpath="{.status.loadBalancer.ingress[0].hostname}")

                            if [ ! -z "$LB_URL" ]; then
                                echo "-------------------------------------------------------"
                                echo "🎉 Deployment Successful!"
                                echo "Your Spring Boot Application is Live At:"
                                echo "👉 http://$LB_URL/success"
                                echo "-------------------------------------------------------"
                                exit 0
                            fi

                            echo "Still waiting for LoadBalancer... retrying in 10s"
                            sleep 10
                        done

                        echo "❌ ERROR: LoadBalancer URL not available yet."
                        exit 1
                    '''
                }
            }
        }
    }

    post {
        failure {
            echo "🚨 Pipeline Failed!"
        }
        success {
            echo "✅ Pipeline Completed Successfully!"
        }
    }
}
