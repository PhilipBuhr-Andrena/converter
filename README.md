# Converter

We often use similar Objects in different layers of our applications, 
like models for our business logic, DTOs for communication with other services and JPA Entities for Databases.
This forces us to write a lot of trivial conversion logic. With this Converter, all you have to do is to annotate your classes, 
and the annotation processor generates Converters for you.    

##Getting Started:

###With gradle:
apt-plugin (+apt idea/eclipse)\
implementation annotations\
annotationProcessor processor

###with maven:
maven-compile plugin -> annotation processor

##Usage:

The main functionality is covered by two annotations `@Converter` and `@ConversionSource`. The reason for two different Annotations is, 
that the business model will be annotated with `@Converter` and all the corresponding DTOs with `@ConversionSource`. 
A converter will be generated, which can convert DTOs to the Model and the other way around, but not from one DTO to another. 
This is supposed to encourage Hexagonal Architecture.   

###Basic
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
- Fields have the same Name or a `@Mapping` Annotation (see section Mapping)
- Fields have the same type or and `@ConversionAdapter` exists (see section ConversionAdapters)
- Fields that have no corresponding field in the target class are ignored.

###Converter Name

The `@Converter` can be given a optional name value `@Converter(name= "Foo")`. By default the name is the simple Class name. 
This name is used for mapping and for the name of the converter. `@Converter(name = "CustomName")` will result in `CustomNameConverter` and 
the ConversionSources have to have the same name `@ConversionSource(name= "CustomName")`.

###ConversionSource

There can be multiple classes annotated with `@ConversionSource` for every Model with `@Converter`. The converter class will have two 
methods for each ConversionSource, one converting the Source to the Model, and one converting the Model to the Source.\
The name value of the `@ConversionSource` has to match the name value of the `@Converter`, if specified. 
If the `@Converter` name value has been omitted, it has to match the Model class name.

###Mapping

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

###ConversionAdapters

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

