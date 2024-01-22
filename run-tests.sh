pushd pa1/src
jbang build miniJava/Compiler.java
echo "Running TestA"
jbang run miniJava/Compiler.java ../../test-files/TestA.java
echo "Running TestB"
jbang run miniJava/Compiler.java ../../test-files/TestB.java
echo "Running TestComments"
jbang run miniJava/Compiler.java ../../test-files/TestComments.java
echo "Running TestEasy"
jbang run miniJava/Compiler.java ../../test-files/TestEasy.java
echo "Running TestFields"
jbang run miniJava/Compiler.java ../../test-files/TestFields.java
echo "Running TestMultipleMethodDeclarations"
jbang run miniJava/Compiler.java ../../test-files/TestMultipleMethods.java
echo "Done"
popd
