# Converter

We often use similar Objects in different layers of our applications, 
like models for our business logic, DTOs for communication with other services and JPA Entities for Databases.
This forces us to write a lot of trivial conversion logic. With this Converter, all you have to do is to annotate your classes, 
and the annotation processor generates Converters for you.    

## Getting Started:

The Converter is not yet hosted on Maven Central. Therefore you need to build the artifacts `annotation-<version>.jar`
and `processor-<version>-all.jar` yourself.\
In a commandline: 
```
$ git clone https://github.com/PhilipBuhr-Andrena/converter.git

$ cd converter

$ ./gradlew buildArtifacts
```

The jar files are now in the `artifacts/` directory. You can copy them to `yourProject/libs/`. 

### With gradle:
Add the jar files to your repositories:
```
// in your build.gradle

repositories {
    // other repositories
    flatDir {
        dirs 'libs' // or where else you put the jars
    }
}
```
If your gradle (wrapper) is below version 5.x.x, I highly recommend the `net.ltgt.apt` plugin with the plugin for your IDE. 
The IDE specific plugin is always helpful, even if you have the newest gradle version, since it marks the generated folders as source folders.

```
// in your build.gradle

plugins {
  id "net.ltgt.apt" version "0.21"
  id "net.ltgt.apt-idea" version "0.21" // if you use Intellij
  id "net.ltgt.apt-eclipse" version "0.21" // if you use Eclipse
}
```
Now you can add the dependencies:
```
// in your build.gradle

dependencies {
    implementation name:'annotation-0.1.0-SNAPSHOT'
    annotationProcessor name: 'processor-0.1.0-SNAPSHOT-all'
    // other dependencies
}
```
### With maven:
With maven, you have to install the jars locally before you can add them as dependencies. Add the following to your plugins, after you copied the jars 
to your `libs/` directory:
```
<!-- in your pom.xml -->

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-install-plugin</artifactId>
            <version>2.5.1</version>
            <executions>
                <execution>
                    <id>install-annotation</id>
                    <goals>
                        <goal>install-file</goal>
                    </goals>
                    <phase>validate</phase>
                    <configuration>
                        <groupId>de.andrena.converter</groupId>
                        <artifactId>annotation</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                        <packaging>jar</packaging>
                        <file>${basedir}/libs/annotation-0.1.0-SNAPSHOT.jar</file>  <!-- path to where you put the jars -->
                        <generatePom>true</generatePom>
                    </configuration>
                </execution>
                <execution>
                    <id>install-processor</id>
                    <goals>
                        <goal>install-file</goal>
                    </goals>
                    <phase>validate</phase>
                    <configuration>
                        <groupId>de.andrena.converter</groupId>
                        <artifactId>processor</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                        <packaging>jar</packaging>
                        <file>${basedir}/libs/processor-0.1.0-SNAPSHOT-all.jar</file> <!-- path to where you put the jars -->
                        <generatePom>true</generatePom>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
__Important:__ You need to `mvn install` now before you add the dependencies. The jars will be added to your local repository cache.\
Then you can then add a normal dependency on the annotations and add the annotation processor to the compile plugin:
```
<!-- in your pom.xml -->

<dependencies>
    <!-- other dependencies... -->
    
    <dependency>
        <groupId>de.andrena.converter</groupId>
        <artifactId>annotation</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.0</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>de.andrena.converter</groupId>
                        <artifactId>processor</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
        <!-- ...other plugins -->

```

## Usage:

The main functionality is covered by two annotations `@Converter` and `@ConversionSource`. The reason for two different Annotations is, 
that the business model will be annotated with `@Converter` and all the corresponding DTOs with `@ConversionSource`. 
A converter will be generated, which can convert DTOs to the Model and the other way around, but not from one DTO to another. 
This is supposed to encourage Hexagonal Architecture.   

### Basic
```java
@Converter
public class Foo {
    public String name;
}

@ConversionSource(name = "Foo")
public class FooDto {
    public String name;
}
```
After adding the annotations you should run a build to trigger the code generation.
The Converter can be used as follows:
```
FooDto fooDto = new FooDto();
fooDto.name = "someName";
Foo foo = FooConverter.createFoo(fooDto);
System.out.println("Foo name: " + foo.name);    // >> FooName: someName
```

__Important:__ \
The code generation is based on the following assumptions:
- Both classes have an empty default Constructor
- Fields are either public or have an getter and setter following standard Java conventions `public void setName(String name)` and `public String getName()`
- Fields have the same Name or a `@Mapping` Annotation (see [Mapping](#mapping))
- Fields have the same type or and `@ConversionAdapter` exists (see [ConversionAdapters](#conversionadapters))
- Fields that have no corresponding field in the target class are ignored.

### Converter Name

The `@Converter` can be given a optional name value `@Converter(name= "Foo")`. By default the name is the simple Class name. 
This name is used for mapping and for the name of the converter. `@Converter(name = "CustomName")` will result in `CustomNameConverter` and 
the ConversionSources have to have the same name `@ConversionSource(name= "CustomName")`.

### ConversionSource

There can be multiple classes annotated with `@ConversionSource` for every Model with `@Converter`. The converter class will have two 
methods for each ConversionSource, one converting the Source to the Model, and one converting the Model to the Source.\
The name value of the `@ConversionSource` has to match the name value of the `@Converter`, if specified. 
If the `@Converter` name value has been omitted, it has to match the Model class name.

### Mapping

Often the field names don't match. In this case use the `@Mapping` Annotation.
```java
@Converter
public class Foo {
    public String name;
}  

@ConversionSource(name= "Foo")
public class FooDto {
    @Mapping("name")
    public String otherName;
}
```

The value of the `@Mapping` Annotation has to match the field name or both fields have `@Mapping` Annotations with matching values.

### ConversionAdapters

If the types of the field are not the same, one can use `@ConversionAdapter` methods. For example if you have:
```java
@Converter
public class Foo {
    public LocalDate date;
} 

@ConversionSource
public class FooDto {
    public String date;
}
```
This would result in an ConversionAdapterNotFoundException during Compile. You need:
```
@ConversionAdapter
public static String myLocalDateConverterMethod(LocalDate date) {
    return date.toString();
}

@ConversionAdapter
public static LocalDate myStringToLocalDateConverterMethod(String date) {
    return LocalDate.parse(date);  //errorhadling omitted
}
```

__Important__
- The methods need to be `public` and `static`
- They can be located anywhere, recommended is a ConfigurationClass
- The name is irrelevant, the correct method is identified by its signature
- As of now, each signature may only exist once. Otherwise a DuplicateMappingNameException is raised during Compile.

### Ignore

Fields, which have no counterpart in the corresponding Class are ignored. But you can also add an `@Ignore` annotation to fields 
in order to prevent them from being mapped if a corresponding field does exist.

## Feedback

Thank you for using the Converter. Please leave feedback at https://github.com/PhilipBuhr-Andrena/converter
