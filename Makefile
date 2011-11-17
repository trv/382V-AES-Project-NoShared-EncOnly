CC = scc
RM = rm -f

COPTS = 	-g -vv -ww
COPTSFAST = 	-vv -ww -O

MAINFILE = 	test

EXEFILE = 	./test

all: 	
	$(CC) $(MAINFILE) $(COPTS)

clean:
	$(RM) *~
	$(RM) $(EXEFILE) $(TESTFILE) $(EXEFILE).cc $(EXEFILE).o $(EXEFILE).h $(EXEFILE).si $(EXEFILE).sir

test:	all
	$(EXEFILE) 

testfast:
	$(CC) $(MAINFILE) $(COPTSFAST)
	$(EXEFILE)

cleansir:
	$(RM) *.sir
