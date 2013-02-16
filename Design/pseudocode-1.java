PSEUOCODEZ

---------------------TASK--------------------


Float calculateDuration()
	//returns the duration of the task in days
	int endDate = (this.endDate.Days) //  gets the day part of the endDate
	int startDate = (this.startDate.Days)
	return (endDate - startDate)
END METHOD


Date getLateStart()
	lateStart = lateEnd.Days - duration
	return lateStart
END METHOD

Date getLateEnd()
	//get parent of task

	parent = THIS.parent


	//search for other tasks with same parent, return an array
	//search array for tasks dependant on this (task), return an array of tasks that depend

	nodesThatDepend = array = []
	for all TASK in tasks (arrayList of all tasks)
		if Task.parent = parent && THIS is an element of Task.dependantNodes
			children.append(TASK)
		END ID
	END LOOP

	
	//search array of tasks that depend for earliest start date, (Date earlyStart = earliest startDate)
	earlyStart = 9999
	dateToPick = null
	for all TASK in nodesThatDepend
		if TASK.startDate.toInt < earlyStart
			earlyStart = TASK.startDate.toInt
			dateToPick = TASK.startDate
		END IF
	END LOOP

	lateEnd = dateToPick
	return lateEnd
END METHOD


Float calculateSlack()
	slack = lateStart - startDate
	return slack
END METHOD

void complete()
	isComplete = true
END METHOD

---------------------CHART------------------------


void drawNode(int x1, int y1, int x2, int y2);
	//draw box
	GUI.drawBox(x1, y1, x2, y2) //draws the box
	midPointX = (x2 + x1) / 2
	midPointY = (y1 + y2) / 2
	GUI.drawLabel(midPointX, midPointY, task.getName())
END METHOD

abstract int[] calculateNextNodeCoords()
	return null
END METHOD


abstract void drawChart()
END METHOD

void changeNodeCoords(int x1, int y1, int x2, int y2)
	currentNodeCoords = [int x1, int y1, int x2, int y2]
END METHOD

-----------------GANTT---------------------

DRAWNODE, CHANGECURRENTNODECOORDS INHERITS FROM CHART CLASS

int[] getNextNodeCoords()
	newX1, newX2, newY1, newY2 = currentNodeCoords //parent node coords
	newX1 = startDateX
	newY1 = oldY2
	newX2 = endDateX
	newY2 = newY1 - boxheight
	int[] newCoords = [newX1, newY1, newX2, newY2]
	return newCoords
END METHOD
	
void sortTasks()
	//called from main before rendering
	for Task[] LEVEL in tasks
		SORT EACH LEVEL INTO ORDER OF START DATE
	END LOOP
END METHOD

void drawChart(task currentNode,int LEVEL)
	//make sure tasks are sorted before entering this algorithm
	//when called, LEVEL is set to 0, currentNode is set as tasks[LEVEL][0] (root)
	//currentNode coords are set at initial values from the main
	currentNode.drawNode(currentNodeCoords) //draw node
	LEVEL++
	if LEVEL < Length(tasks) //is LEVEL in range
		//draws out all children of initial currentNode
		//any nodes not connected will not be drawn
		For task currentTask in tasks[LEVEL]
			//checks to see if the nodes are children, if yes, draws them
			if currentTask.getParent = currentNode
				drawChart(currentTask, LEVEL)
			END IF
		END LOOP
	END IF
END METHOD

----------WBT----------

void drawChart(arrayList level, parent) // OMARS
	i=0
	for i < length of level
		arraylist newLevel = get all children of level[i]
		currentNode = level.get(i)
		drawNode(currentNode)

		if parent not null then
			draw line from node of currentNode to parent
		end if

		if newLevel arrayList not null then
			j=0;
			for j < length of newLevel
				drawChart(newLevel, currentNode)
			end for
		end if
	end for
end method


void drawChart(X, Y, currentNode) // TODDS
	//nodes 100 x, 120 y
	drawNode(currentNode)
	arraylist children = currentNode.getChildren()
	lineMult = len(children) // number of children
	lineSize = (len-1) * 120 + (len-1) * 50 // gap = 50, boxWidth = 120
	drawLine(x, y+60, x+50, y+60) // 60 cause boxwidth = 120, 60 = 120/2
	curX = x+50 // update x & y
	curY = y+60
	lineHalf = lineSize / 2
	drawLine(curX - lineHalf, curY, curX + lineHalf, curY) // if lineSize = 0, then draws a dot
	
	curX = x-Linehalf
	for c in children
		drawLine(curX, curY + 20)
		drawChart(curX, curY + 20, c)
		curX + 120 + 50 // move one gap and one box along
	end for
end method

void drawNode(node) // TODDS
	drawBox(currentX, currentY, currentX + 100, currentY + 120)
	drawText(node.name, currentX + 50, currentY + 60)
end method
	
	
	

--------------PERT-----------
			
void drawNode(nodeX, nodeY)
	// nodes are 100 by 80
	// nodeX and nodeY are the new node coords
	drawRectangle(nodeX, nodeY, nodeX + 100, nodeY + 80)
	// main node
	drawRectangle(nodeX, nodeY, nodeX + 30, nodeY + 20)
	drawRectangle(nodeX + 30, nodeY, nodeX + 70, nodeY + 20)
	drawRectangle(nodeX + 70, nodeY, nodeX + 100, nodeY + 20)
	// top info panels
	drawRectangle(nodeX, nodeY + 80, nodeX + 30, nodeY + 100)
	drawRectangle(nodeX + 30, nodeY + 80, nodeX + 70, nodeY + 100)
	drawRectangle(nodeX + 70, nodeY + 80, nodeX + 100, nodeY + 100)
end method

Node[] getNodesDependant(node)
	array = all Nodes
	newArr = []
	for (current in array)
		if (current is dependant on node)
			newArr.append current
		end if
	end loop
	return newArr
end method


Array getCriticalPath(currentNode)
	critPath = [] // empty array
	// current node slack will = 0, as it's the first node
	boolean canContinue = True
	while canContinue // O(n)
		nodesDep = getNodesDependant(currentNode) // get the nodes that are dependant and current
		for (node in nodesDep) // O(n^2)
			if (node.slack == 0)
				critPath.append {currentNode, node} // append to array
				Task nextNodes = node // set up for next node
			end if
			if (end of tasks reached)
				canContinue = False // loop can end
			else
				currentNode = node // move to next node
			end if
		end loop
	end loop
	return critPath
end method
				
			
	
	
		
	


void drawChart(X, Y, currentNode)
	// current node is the first called as the initial node
	drawNode(X, Y) // draw first node
	// nodes are 100x80, gaps are 50
	Y = Y + 40 // centred on node

	drawLine(X + 100, Y, X + 120, Y) // first bit of line
	// 20 long, plus the 100 leeway from the node

	work = len(getNodesDependant(currentNode)) - 1
	LEN = [work * 80] + [work * 50]
	drawLine(X + 120, Y - LEN/2, X + 120, Y + LEN/2) // the split
	
	nextNodes = getNodesDependant(currentNode)
	renderHeight = (LEN / 2) + 40 // half above the top of the split, half below
	if (len(nextNodes) != 0) // can draw more stuff
		for (newNode in nextNodes)
			drawChart(X + 120, renderHeight, newNode)
			renderHeight += 130 // 80 + 50, next node is drawn 130 pixels below the last one
		end loop
	end if
end method

	
	

		
		
	


