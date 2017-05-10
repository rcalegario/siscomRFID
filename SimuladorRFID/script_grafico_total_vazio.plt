set xlabel "Número de etiquetas"
set ylabel "Número total de slots vázio"
set key vertical top left
set grid
set pointsize 2
set terminal png
set output './results/vazio.png'
plot "lowerbound_100_100_1000_2000_64.txt" u 1:4 t 'lowerbound' w linespoints pt 1,"eom-lee_100_100_1000_2000_64.txt" u 1:4 t 'eom-lee' w linespoints pt 2,"chen_100_100_1000_2000_64.txt" u 1:4 t 'chen' w linespoints pt 3,"vahedi_100_100_1000_2000_64.txt" u 1:4 t 'vahedi' w linespoints pt 4
