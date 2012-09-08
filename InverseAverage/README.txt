This is the "Media Inversa" or "Inverse Average" as stated in:
http://albaontech.wordpress.com/2012/06/25/kata-media-inversa

Given the soft restrictions we can just calculate all possibilities and cache them.
1 to 10 numbers can be used to get the average, so for each count we loop for each possible sum of those numbers (count to count*10), we calculate the average and use that average as the key to look up.
There are only 289 unique possible averages what can be got from 1-10 numbers from 1 to 10.
There are 505 non-unique averages.

Please notice that if you know the sum and the count, it's very easy to get a combination of the numbers which fullfill those conditions.
 
