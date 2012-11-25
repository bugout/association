default: Test.class

Test.class: src/AssociationsFinder.java src/data/*.java src/generator/*.java
	javac -cp "src" src/AssociationsFinder.java

clean:
	rm *.class data/*.class generator/*.class
