pipeline {
    
    agent any

    environment {
        CI = true
        ARTIFACTORY_ACCESS_TOKEN = credentials('artifactory-access-token')
        KubeServerIP = credentials('KubeServerIP')
        KubeServerUsername = credentials('KubeServerUsername')
        KubeServerPassword = credentials('KubeServerPassword')
        KubeUser = credentials('KubeUserID')
        Port = credentials('KubeServerSSHPort')
        Artifactory = credentials('ArtifactoryIP') 
        DockerID = credentials('DockerHubUser')
        // DevZone = credentials('DevBackZone')
        DevZone = "geekbackend"
        VERSION = "${BUILD_NUMBER}"
        // KeyTo = credentials('PathToKeyStore') 
        // KeyFrom = credentials('PathFromKeyStore') 
    }
    
    stages{

    

        // stage('Clean Workspace') {
        //     steps {
        //         cleanWs()
        //     }
        // }
            
        stage('Git Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CCS-2022/Geek-Service.git'
            //SSL Key CP
            // sh 'cp ${KeyFrom} ${KeyTo}'
            }
        }

        stage('Building Project') {
            steps {
                sh "./gradlew wrapper --gradle-version 8.4"
                sh "./gradlew clean build -x test"
            }
        }

        // stage('Testing Build') {
        //     steps {
        //         sh "./gradlew test -x test"
        //     }
        // }

        // stage('Code Quality Check via SonarQube') {
        //     steps {
        //         withSonarQubeEnv("SecretSantaSonar") {
        //             sh "./gradlew sonar"
        //         }
        //     }
        // }

        // stage('Upload to Artifactory') {
        //     agent {
        //         docker {
        //             image 'releases-docker.jfrog.io/jfrog/jfrog-cli-v2:2.2.0' 
        //             reuseNode true
        //         }
        //     }
        //     steps {
        //         sh 'jfrog rt upload --url http://${Artifactory}/artifactory/ --access-token ${ARTIFACTORY_ACCESS_TOKEN} ./build/libs/*SNAPSHOT.jar ss-${ENVS}/'
        //         }
        // }


        stage('Create Image && Upload to DockerHub') {
            steps {
                script {
                    try {
                        sh 'docker image rm ${DockerID}/${DevZone}:DEV'
                        } catch (Exception e) {
                            echo "Image does not exist. Error: ${e}"
                        }
                    }
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        docker build -t ${DockerID}/${DevZone}:DEV .
                        docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                        docker push ${DockerID}/${DevZone}:DEV
                    '''
                }
            }
        }     

        stage('Update Deployment YAML') {
            steps {
                script {
                    // Define the version number or label value to update
                    def versionNumber =  env.VERSION

                    // Define the path to the deployment YAML file
                    def deploymentYamlFile = 'deployment-backend.yaml'

                    // Read the content of the deployment YAML file
                    def yamlContent = readFile(deploymentYamlFile)

                    // Perform string replacement to update the label or annotation value
                    yamlContent = yamlContent.replaceAll(/version: ".+"/, "version: \"${versionNumber}\"")

                    // Write the modified content back to the deployment YAML file
                    writeFile file: deploymentYamlFile, text: yamlContent

                }
            }
        }

        stage("Deploy To Kubernetes"){
            steps{
                script{ 
                    def remote = [:]
                    remote.name = 'K8S master'
                    remote.host = env.KubeServerIP
                    remote.user = env.KubeServerUsername
                    remote.password = env.KubeServerPassword
                    remote.allowAnyHosts = true
            
                    stage('Place yaml file into kube master to deploy') {
                        sshPut remote: remote, from: 'deployment-backend.yaml', into: './geek-backend'  
                    }

                    stage('Deploy Geek-Backend') {
                        sshCommand remote: remote, command: "microk8s kubectl apply -f ./geek-backend/deployment-backend.yaml -n geek --force"
                    }
                }
            }
        }

        stage("Cleaning Up Storage Space"){
            steps{
                sh 'docker system prune -af'
            }
        }
    }
}


