# Projet Génie Logiciel - Team of 5.
<pre>
This project aims to design, implement, and optimize a compiler for the Deca programming language -similar to Java).
The primary objective of this project is to create a robust compiler that translates Deca source code into executable machine code, facilitating the efficient execution of Deca programs on various platforms.
</pre>
Our project is structured in a way that allows the validation of all its components through a single command, triggering the execution of each test block with an immediate halt in case of failure. To initiate these tests, you have two commands at your disposal after positioning yourself in the root directory:
<pre>
  mvn test
</pre>
This command runs the tests without performing test coverage calculation.
<pre>
  mvn verify
</pre>
On the other hand, this command runs the tests while simultaneously calculating the test coverage using Jacoco.

For further information about how to run tests, you can refer to our documentation given in the directory /docs/Tests.pdf.
