# Truth Table Printer

This is the java code for my blog post series on printing truth tables.

[Here is the relevant post.](https://cas-e.github.io/post/java-bool-table.html)


## Program behavior

When the program has an input string like:

~~~
"p || q && !r && q"
~~~

It will print:

~~~
p     q     r     | formula
---------------------------
false false false | false
false false true  | false
false true  false | true
false true  true  | false
true  false false | true
true  false true  | true
true  true  false | true
true  true  true  | true
---------------------------
~~~