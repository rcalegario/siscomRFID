javac -d bin/ -cp src src/simulador/Plot.java 
javac -d bin/ -cp src src/simulador/Simulador.java 
javac -d bin/ -cp src src/simulador/Principal.java

java -cp ./bin simulador.Principal

gnuplot script_grafico_total_colisao.plt
gnuplot script_grafico_total_slots.plt
gnuplot script_grafico_total_vazio.plt

open results/colisao.png
open results/slots.png
open results/vazio.png
