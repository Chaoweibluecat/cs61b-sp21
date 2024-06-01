my cs61b implementation 
# Proj2 and other labs
see cs61b-23fall


# Proj3

MyWorldImpl implements most of proj3 

<img width="792" alt="image" src="https://github.com/Chaoweibluecat/cs61b-sp21/assets/55312380/53d337fd-ff13-4a27-b401-689a29e73782">

welcome page , only supports New Game; Load&Save may come later

use interact by **keyboard** as default (working on Network by it seems not really meaningful)



<img width="951" alt="image" src="https://github.com/Chaoweibluecat/cs61b-sp21/assets/55312380/b648102f-ee9b-4c90-9e77-c9c7b6d0ee93">

My Algorithm presented as follows

1.Generate random rooms number between 8-16 As **roomNum**;   Randomly generate Rooms ,abort if illegal,until room numbers reaches **roomNum**;

2.Calculate  **Manhattan Distance**  between **each pair of room ** (least manhattan distance among all points from edge points of RoomA to RoomB), use it as the weight of graph edge

3.now we have a graph with edges, use **Kruskal** to generate a **minimal spanning tree;**

4.now we have all target links between rooms; for each picked edge, we pick a point on the source room, finds its way to the target room,（take a random step as long as it's legal , the world legal means it should ideally shorten the Manhattan Distance to the target room) ; use the visited path as hallway;

5.now we have a connected graph of rooms and hallways, we can generate walls and locked door , and the player as well

the project is yet to polish, so there's still buch of  trivial stuff to accomplish

