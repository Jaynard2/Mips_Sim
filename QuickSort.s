-- R1 is lower pointer
-- R2 is upper pointer
-- R3 is pivot pointer
-- R4 is lower value
-- R5 is upper value
-- R6 is pivot value
-- R7 is work reg
-- R30 is SP
-- R8 is left edge of array
Begin Assembly
addi R30, R0, 5000
addi R1, R0, 4000
addi R2, R0, 4000
addi R3, R0, 4996
jal sort
halt
LABEL sort
beq R1, R3, finshed
add R8, R0, R1
lw R4, 0(R1)
lw R5, 0(R2)
lw R6, 0(R3)
LABEL partition
sub R7, R4, R6
bgtz R7, walkUpper
addi R1, R1, 4
beq R1, R2, continue
addi R2, R2, 4
lw R5, 0(R2)
LABEL continue
lw R4, 0(R1)
beq R1, R3, finshIter
j partition
LABEL walkUpper
sub R7, R5, R6
bltz R7, swap
addi R2, R2, 4
beq R2, R3, finshIter
lw R5, 0(R2)
j walkUpper
LABEL swap
sw R4, 0(R2)
sw R5, 0(R1)
addi R1, R1, 4
addi R2, R2, 4
lw R4, 0(R1)
lw R5, 0(R2)
beq R1, R3, finshIter
beq R2, R3, finshIter
j partition
LABEL finshIter
sw R6, 0(R1)
sw R4, 0(R3)
sw R31, 0(R30)
addi R30, R30, 4
sw R2, 0(R30)
addi R30, R30, 4
sw R1, 0(R30)
addi R30, R30, 4
add R2, R0, R8
addi R3, R1, -4
add R1, R0, R8
sub R7, R1, R3
bgtz R7, noLeft
jal sort
LABEL noLeft
addi R30, R30, -4
lw R1, 0(R30)
sw R0, 0(R30)
addi R30, R30, -4
lw R3, 0(R30)
sw R0, 0(R30)
add R2, R0, R1
jal sort
addi R30, R30, -4
lw R31, 0(R30)
sw R0, 0(R30)
LABEL finshed
jr R31
End Assembly
Begin Data 4000 250
195
728
943
955
102
116
747
169
278
34
415
288
919
290
492
993
508
244
664
417
352
115
79
272
111
842
212
985
929
2
18
765
425
233
218
736
607
605
316
397
92
287
451
922
217
816
948
912
240
973
897
235
831
942
587
482
185
104
694
265
428
251
624
953
540
538
963
865
178
954
179
500
81
152
810
841
310
764
707
427
204
561
913
698
35
485
97
550
341
252
503
756
32
782
807
309
77
420
704
776
82
965
700
28
125
609
271
367
121
86
513
493
839
755
65
647
645
182
656
763
741
622
356
407
817
400
721
59
357
718
108
487
849
931
956
711
403
273
906
648
363
293
398
159
890
53
770
372
383
225
636
749
928
443
604
516
504
9
376
829
986
699
466
386
445
486
881
722
173
498
213
785
751
260
276
808
515
113
995
775
589
825
49
303
14
952
884
915
46
667
644
134
479
744
771
294
66
299
783
514
710
476
682
431
797
241
248
509
905
132
399
75
517
199
818
279
652
778
650
523
828
529
774
937
794
851
145
164
975
758
861
421
230
194
856
544
3
610
863
105
532
798
480
573
891
641
379
286
95
333
End Data
Begin Data 5000 1024
End Data