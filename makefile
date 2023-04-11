# Define the compiler and flags
JAVAC = javac
JAR = jar
SRCFILES = $(shell find $(SRCDIR) -name '*.java')

# Define the source, class, and JAR file paths
SRCDIR = src
BINDIR = bin
LIBDIR = lib
JARFILE = GatorTaxi.jar
MANIFEST = manifest.txt

# Find all the .java files in the source directory
SOURCES := $(shell find $(SRCDIR) -name '*.java')

# Convert the .java files to .class files in the bin directory
CLASSES := $(SOURCES:$(SRCDIR)/%.java=$(BINDIR)/%.class)

# Define the targets
.PHONY: all clean

all: $(JARFILE)

$(JARFILE): $(CLASSES)
	$(JAR) cfm $(JARFILE) $(MANIFEST) -C $(BINDIR) .

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) $(JFLAGS) -d $(BINDIR) $(SRCFILES) $<

clean:
	rm -rf $(BINDIR)/*.class $(JARFILE)