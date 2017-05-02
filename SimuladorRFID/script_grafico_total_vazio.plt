set xlabel "Número de etiquetas"
set ylabel "Número total de slots vázio"
set key vertical top left
set grid
set pointsize 2
plot "lowerbound_100_100_1000_2000_64.txt" u 1:4 t 'lowerbound' w linespoints, \ 
"eom-lee_100_100_1000_2000_64.txt" u 1:4 t 'eom-lee' w linespoints, \ 
