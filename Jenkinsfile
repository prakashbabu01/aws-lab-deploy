pipeline
{
    agent any

    tools {
    	    maven "MAVEN3" //Specify tools to be used exacty as given in tools section of manage jenkins
    	    jdk "OracleJDK8"
    	}



    environment {
        s3BucketName = "prakash-terraform-app-states"
        s3Path = "/cloudformation-templates/"
        templateName = "ec2-template.yaml"
        templatePath = "./cloudFormationTemplates/"
        paramPath = "./paramFiles/"
        stackName = "cf-webserver-ec2"
        instanceName = "webserv-tomcat"
    }

    stages
  {
        stage('Fetch code') {
          steps{
              git branch: 'main', url:'https://github.com/prakashbabu01/aws-lab-deploy.git'
          }
        }

        stage('Check Template/param files existance '){
            steps {
            if (fileExists("$templatePath/$templateName")) {
                            echo "File $templateName found!"
            }
            if (fileExists("$paramPath/$instanceName.parm")) {
                            echo "File $instanceName.parm found!"
                        }

       if ( fileExists("$paramPath/$instanceName.tags") ) {
              echo "File $instanceName.tags found!"
                                    }
        }




 //       stage('Deploy to s3') {
 //                 steps {
 //               withAWS(credentials: 'awscredsjenkins_awscreds', region: 'us-east-1') {
 //                 s3Upload( bucket: "$s3BucketName", path: "$s3Path", file: "$filePath" + "$templateName"  )
 //                 sh "aws cloudformation deploy --template-file "$filePath" + "$templateName" --stack-name $stackName --parameter-overrides Key1=Value1 Key2=Value2 --tags Key1=Value1 Key2=Value2"
//
//                }
//             }
//             }

    //stage('cleanup code'){
    //            steps {
    //                sh 'rm -rf ./cloudFormationTemplates'
    //            }
    //        }
  }
}