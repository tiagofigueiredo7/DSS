# Configurações
SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib

# Driver MySQL
MYSQL_JAR = $(LIB_DIR)/mysql-connector-j-9.4.0.jar

# Classpath
CLASSPATH = $(BIN_DIR):$(MYSQL_JAR)

# Encontrar todos os ficheiros .java
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

# Alvos principais
.PHONY: all compile run clean

all: compile

# Compilar todos os ficheiros Java
compile:
	@mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) -cp $(CLASSPATH) $(SOURCES)

# Executar o programa
run: compile
	java -cp $(CLASSPATH) Main

# Limpar ficheiros compilados
clean:
	rm -rf $(BIN_DIR)/*
