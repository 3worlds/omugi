#!/usr/bin/python

import getopt
import sys
import os
import time

def srcPath(path, project):
	return os.path.join("..", "src", project, path, project)
	
	
def genTable(dataType, longDataType):
	capitalisedDataType = dataType.capitalize()
	fileName = sys.argv[1].replace('Type', capitalisedDataType).replace('.template', '.java')
	print
	print "Generating " + fileName + " for data type " + dataType + " from input " + sys.argv[1]
 	print
 	
 	with open(sys.argv[1]) as input:
		inputLines = input.readlines()
    	
 	output = open(fileName,'w')
	for line in inputLines:
		line = line.replace('$type$', dataType)
		line = line.replace('$Type$', capitalisedDataType)
		line = line.replace('$LongType$', longDataType)
		line = line.replace('$Tool$', sys.argv[0])
		line = line.replace('$Date$', time.strftime("%c"))
		output.write(line)
	output.close()
 	

def main():
	genTable("byte", "Byte")
	genTable("short", "Short")
	genTable("int", "Integer")
	genTable("long", "Long")
	genTable("float", "Float")
	genTable("double", "Double")
	genTable("char", "Character")
	genTable("boolean", "Boolean")
	
if __name__ == "__main__":
    main()
