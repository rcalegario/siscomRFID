javac -d bin/ -cp src src/simulador/Plot.java 
javac -d bin/ -cp src src/simulador/Simulador.java 
javac -d bin/ -cp src src/simulador/Principal.java

java -cp ./bin simulador.Principal

gnuplot script_grafico_total_colisao.plt
gnuplot script_grafico_total_slots.plt
gnuplot script_grafico_total_vazio.plt
gnuplot script_grafico_tempo_medio.plt
gnuplot script_q_grafico_total_slots.plt

open results/colisao.png
open results/slots.png
open results/vazio.png
open results/tempo.png
open results/slotsq.png
