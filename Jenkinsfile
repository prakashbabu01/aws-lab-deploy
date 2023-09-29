pipeline
{
    agent any

    tools {
    	    maven "MAVEN3" //Specify tools to be used exactly as given in tools section of manage jenkins
    	    jdk "OracleJDK8"
    	}



    environment {
        s3BucketName = "prakash-terraform-app-states"
        s3Path = "/cloudformation-templates/"
        templateName = "ec2-template.yaml"
        templatePath = "./cloudFormationTemplates/"
        paramPath = "./paramFiles"
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

stage ('Check for existence of index.html') {

    when {
            expression {
                return fileExists("${templatePath}${templateName}");
            }
        }
        steps {
            sh "sh ./Shell/paramFileExtractValues.sh ${paramPath}/${instanceName}/${instanceName}.param  ${paramPath}/${instanceName}/${instanceName}.tags"
            echo "${paramPath}/${instanceName}/${instanceName}.tags"
            echo "${paramPath}/${instanceName}/${instanceName}.param"
        }
}

stage ('print payload') {


        steps {
            sh "echo $payload"

        }
}


stage ('deploy ec2 with cf stack') {

    environment {

        para_args = readFile("param.tmp")
        para_tag_args = readFile("param-tags.tmp")

    }

        steps {
             withAWS(credentials: 'awscredsjenkins_awscreds', region: 'us-east-1') {
                 sh 'echo "value of para args is "$para_args'
                 sh 'echo "value of para args is "$para_tag_args'
            sh 'aws cloudformation deploy  --template-file ${templatePath}${templateName} --stack-name ${stackName} --parameter-overrides $para_args --tags $para_tag_args'

             }
        }


}




//       stage('Deploy to s3') {
//                  steps {
 //               withAWS(credentials: 'awscredsjenkins_awscreds', region: 'us-east-1') {
 //                 s3Upload( bucket: "$s3BucketName", path: "$s3Path", file: "$filePath" + "$templateName"  )
 //                 sh "aws cloudformation deploy --template-file "$filePath" + "$templateName" --stack-name $stackName --parameter-overrides Key1=Value1 Key2=Value2 --tags Key1=Value1 Key2=Value2"
//
//                }
//             }
//             }

    stage('cleanup code'){
                steps {
                    sh 'rm -rf ./cloudFormationTemplates'
                }
            }
  }
  }
