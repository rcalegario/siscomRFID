set xlabel "Número de etiquetas"
set ylabel "Número total de slots"
set monochrome
set key vertical top left
set grid
set pointsize 2
set terminal png
set output './results/slotsq.png'
plot "q_100_100_1000_2000_64.txt" u 1:2 t 'q' w linespoints pt 1
