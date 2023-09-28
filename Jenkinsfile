pipeline
{
    agent any

    tools {
    	    maven "MAVEN3" //Specify tools to be used exacty as given in tools section of manage jenkins
    	    jdk "OracleJDK8"
    	}



    environment {
        s3BucketName = "prakash-terraform-app-states"
        templateName = ""
        s3Path = "/cloudformation-templates/"
        fileName = "ec2-template.yaml"
        filePath = "./cloudFormationTemplates/"
    }

    stages
  {
        stage('Fetch code') {
          steps{
              git branch: 'main', url:'https://github.com/prakashbabu01/aws-lab-deploy.git'
          }
        }

        stage('Test'){
            steps {
                sh 'ls -ltr ./cloudFormationTemplates/*'
            }
        }
        stage('Deploy to s3') {
                  steps {
                withAWS(credentials: 'awscredsjenkins_awscreds', region: 'us-east-1') {
                  s3Upload(bucket: "$s3BucketName"+"$s3Path", sourceFile : "$filePath" + "$fileName"  , )
                }
              }
             }
    stage('cleanup code'){
                steps {
                    sh 'rm -rf ./cloudFormationTemplates'
                }
            }
  }
}