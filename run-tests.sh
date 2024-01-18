pushd pa1/src
echo "Running TestA"
jbang run miniJava/Compiler.java ../../test-files/TestA.java
echo "Running TestB"
jbang run miniJava/Compiler.java ../../test-files/TestB.java
echo "Running TestComments"
jbang run miniJava/Compiler.java ../../test-files/TestComments.java
popd
