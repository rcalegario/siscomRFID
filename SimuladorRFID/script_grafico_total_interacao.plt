set xlabel "Número de etiquetas"
set ylabel "Tempo de Simulação"
set key vertical top left
set grid
set pointsize 2
set terminal png
set output './results/interacao.png'
plot "chen_100_100_1000_2000_64.txt" u 1:6 t 'chen' w linespoints pt 1
