CC = scc
RM = rm -f

SCFILES = \

COPTS = 	-g -vv -ww

MAINFILE = 	test

EXEFILE = 	./test

all: 	$(SCFILES) $(HFILES)
	$(CC) $(MAINFILE) $(COPTS)

clean:
	$(RM) *~
	$(RM) $(EXEFILE) $(TESTFILE) $(EXEFILE).cc $(EXEFILE).o $(EXEFILE).h $(EXEFILE).si $(EXEFILE).sir

test:	all
	$(EXEFILE) 
