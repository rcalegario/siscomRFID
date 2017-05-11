set xlabel "Número de etiquetas"
set ylabel "Tempo de Simulação (ms)"
set key vertical top left
set grid
set pointsize 2
set terminal png
set logscale y
set output './results/tempo.png'
plot "lowerbound_100_100_1000_2000_64.txt" u 1:5 t 'lowerbound' w linespoints pt 1,"eom-lee_100_100_1000_2000_64.txt" u 1:5 t 'eom-lee' w linespoints pt 2,"q_100_100_1000_2000_64.txt" u 1:5 t 'q' w linespoints pt 3,"chen_100_100_1000_2000_64.txt" u 1:5 t 'chen' w linespoints pt 4,"vahedi_100_100_1000_2000_64.txt" u 1:5 t 'vahedi' w linespoints pt 5
