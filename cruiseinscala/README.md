## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

#### Functions
There are 3 functions and here is how to call them once you start sbt:
<ol>
    <li>Run Pricing Function:</li>
        <ul><li>~run pricing 1 d</li></ul>
    <li>Run All Combinable Promotions:</li>
        <ul><li>~run promotion 1 d</li></ul>
    <li>Run Specific Combinable Promotions:</li>
        <ul>
            <li>~run promotion 2 P3</li>
            <li>Where P3 can be replaced with any Promotion name desired</li> 
        </ul>
</ol>

#### Tests

<ul>
    <li>Once you start sbt then just run `test` and it should run all of the desired tests</li>
</ul>

   
