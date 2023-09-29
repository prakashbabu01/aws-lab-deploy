pipeline
{
    agent any

    tools {
            maven 'MAVEN3' //Specify tools to be used exactly as given in tools section of manage jenkins
            jdk 'OracleJDK8'
    }

//below trigger need to be created with all below parameters. keep key and value below inline with values provided in the trigger and token value.
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

    stages() {

        stage('Fetch code') {
            steps {
                git branch: 'main', url:'https://github.com/prakashbabu01/aws-lab-deploy.git'
            }
        }

        stage('set environment variables') {
            steps {
                script {
                    def paramProperty = readYaml file: './jenkinsdeployment-properties.yaml'

                    env.s3BucketName = paramProperty.s3BucketName1
                    env.s3Path = paramProperty.s3Path1
                    env.templatePath = paramProperty.templatePath1
                    env.templateName = paramProperty.templateName1
                    env.paramPath = paramProperty.paramPath1
                    env.paramPathFolder = paramProperty.paramPathFolder1
                    env.stackName = paramProperty.stackName1
                    env.instanceName = paramProperty.instanceName1
                    env.instanceFolderNamePosition = paramProperty.instanceFolderNamePosition1
                // println(env.s3BucketName)
                //                            println(env.s3Path)
                }
            }
        }

        stage('read changes from git and extract parameter files') {
            steps {
                sh ' echo added and modified files are '
                //sh "echo $modified_files"
                sh " echo $added_files "

//below is the groovy code which can be included for any programming needed in the jenkins pipeline and needs to be included as part of script {}.
//important to note all groovy function will work except ones like foreach where collect and iteration is needed. normal looping works.
//groovy code is part of this project seperatly and can be compiled along with intellij to play around


                script {
 //println("values of the variables from param file are")

                        //   println(env.s3BucketName)
                        //   println(env.s3Path)

                    def str_added_files = env.added_files
//file list is not implicitly converted to list data type to iterate. hence trimming off [] on both ends and then splitting it with "," to create list
                    def newParamFilesList = str_added_files[1..-2].split(',')
                    def instancesList = []
                    def instanceFolderNamePosition = env.instanceFolderNamePosition
// we are considering the folder name inside paramFiles directory with above postion to identify instance name.
                    //println(newParamFilesList)
                    //println("size of list is ")
                    //println(newParamFilesList.size())
                    //println( env.paramPathFolder )
                    for (paramFile in  newParamFilesList) {
                        if (paramFile.contains(env.paramPathFolder)) {
                            //println( paramFile.toString())
                            paramFileTokens = paramFile.tokenize('/')
                            instancesList.add(paramFileTokens.get(instanceFolderNamePosition))
                        }
                    }
                    instanceListUnique = instancesList.unique()
                }
            }
        }

        stage('Check for existence of index.html') {
            when {
                expression {
                    return fileExists("${templatePath}${templateName}")
                }
            }
            steps {
                script {
                    env.instanceName = instanceListUnique.get(0)
                }

                sh "sh ./Shell/paramFileExtractValues.sh ${paramPath}/${instanceName}/${instanceName}.param  ${paramPath}/${instanceName}/${instanceName}.tags"
                echo "${paramPath}/${instanceName}/${instanceName}.tags"
                echo "${paramPath}/${instanceName}/${instanceName}.param"
            }
        }

        stage('deploy ec2 with cf stack') {
            environment {
                para_args = readFile('param.tmp')
                para_tag_args = readFile('param-tags.tmp')
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

        stage('cleanup code') {
                steps {
                    sh 'rm -rf ./cloudFormationTemplates'
                }
        }
    }
}
