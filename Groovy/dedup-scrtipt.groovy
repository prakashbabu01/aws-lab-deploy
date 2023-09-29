def str=(["paramFiles/db-mysql/mem-cache.param","paramFiles/db-mysql/mem-cache.tags"]).toString()
def newParamFilesList = str[1..-2].split(',')
def instancesList = []
def instanceFolderNamePosition = 1
println(newParamFilesList)
println("size of list is ")
println(newParamFilesList.size())
for ( paramFile in  newParamFilesList) {
    paramFileTokens = paramFile.tokenize('/')
    instancesList.add( paramFileTokens.get(instanceFolderNamePosition) )
}
instanceListUnique=instancesList.unique()
//instanceListUnique.forEach {print(it)}
println(instanceListUnique.get(0))

