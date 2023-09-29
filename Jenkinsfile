pipeline
{
    agent any

    tools {
    	    maven "MAVEN3" //Specify tools to be used exactly as given in tools section of manage jenkins
    	    jdk "OracleJDK8"
    	}

triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'added_files', value: '$.head_commit.added']
     ],

    causeString: 'Triggered on $added_files',

     token: 'abc1234',
     tokenCredentialId: '',

     printContributedVariables: true,
     printPostContent: true,

     silentResponse: false,

     shouldNotFlatten: false

    )
  }

    environment {
        //s3BucketName = "prakash-terraform-app-states"
        //s3Path = "/cloudformation-templates/"
        //templateName = "ec2-template.yaml"
        //templatePath = "./cloudFormationTemplates/"
        //paramPath = "./paramFiles"
        //paramPathFolder = "paramFiles"
        //stackName = "cf-webserver-ec2"
        //instanceName = "webserv-tomcat"



                s3BucketName = ""
                s3Path = ""
                templateName = ""
                templatePath = ""
                paramPath = ""
                paramPathFolder = ""
                stackName = ""
                instanceName = ""
    }



    stages
  {

stage('init') {
          steps{

           script {

           def paramProperty = readYaml file : 'jenkinsdeployment-properties.yaml'

                           env.s3BucketName = paramProperty.s3BucketName
                           env.s3Path = paramProperty.s3Path
                           env.templateName = paramProperty.templateName
                           env.templatePath = paramProperty.templatePath
                           env.paramPath = paramProperty.paramPath
                           env.paramPathFolder = paramProperty.paramPathFolder
                           env.stackName = paramProperty.stackName
                           env.instanceName = paramProperty.instanceName

           }

          }
        }


        stage('Fetch code') {
          steps{
              git branch: 'main', url:'https://github.com/prakashbabu01/aws-lab-deploy.git'
          }
        }



stage('read changes from git and extract parameter files') {
      steps {

        sh " echo added and modified files are "
        //sh "echo $modified_files"
        sh " echo $added_files "

          script {

def str_added_files = env.added_files
def newParamFilesList = str_added_files[1..-2].split(',')
def instancesList = []
def instanceFolderNamePosition = 1
println(newParamFilesList)
println("size of list is ")
println(newParamFilesList.size())
println( env.paramPathFolder )
for ( paramFile in  newParamFilesList) {
if ( paramFile.contains(env.paramPathFolder) ) {
println( paramFile.toString())
    paramFileTokens = paramFile.tokenize('/')
    instancesList.add( paramFileTokens.get(instanceFolderNamePosition) )
    }
}
instanceListUnique=instancesList.unique()
      }
      }
    }

stage ('Check for existence of index.html') {

    when {
            expression {
                return fileExists("${templatePath}${templateName}");
            }
        }
        steps {

        script {

        env.instanceName=instanceListUnique.get(0)

        }

            sh "sh ./Shell/paramFileExtractValues.sh ${paramPath}/${instanceName}/${instanceName}.param  ${paramPath}/${instanceName}/${instanceName}.tags"
            echo "${paramPath}/${instanceName}/${instanceName}.tags"
            echo "${paramPath}/${instanceName}/${instanceName}.param"
        }
}

//stage ('deploy ec2 with cf stack') {

//    environment {

//        para_args = readFile("param.tmp")
//        para_tag_args = readFile("param-tags.tmp")

//    }

//        steps {
//             withAWS(credentials: 'awscredsjenkins_awscreds', region: 'us-east-1') {
//                 sh 'echo "value of para args is "$para_args'
//                 sh 'echo "value of para args is "$para_tag_args'
//            sh 'aws cloudformation deploy  --template-file ${templatePath}${templateName} --stack-name ${stackName} --parameter-overrides $para_args --tags $para_tag_args'
//
//             }
//        }


//}




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
