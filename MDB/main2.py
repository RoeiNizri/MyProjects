# Hanil Zabrailov 319331419
# Roee Nizri 318260742

class Node:
    """
    A class representing a node in a graph, where each node has a unique identifier,
    a color, and a list of neighboring nodes.

    Attributes:
        node_id (str): The unique identifier of the node.
        color (int): The color assigned to the node.
        neighbors (list[Node]): A list of other Node instances that are considered
            neighbors to this node in the graph.
    """

    def __init__(self, node_id: str, color: int):
        """
        Initializes a new instance of the Node class.

        Parameters:
            node_id (str): The unique identifier for the node.
            color (int): The color to be assigned to the node.
        """
        self.node_id = node_id
        self.color = color
        self.neighbors: list['Node'] = []

    def get_node_id(self) -> str:
        """Returns the unique identifier of the node."""
        return self.node_id

    def set_node_id(self, node_id: str):
        """
        Sets a new unique identifier for the node.

        Parameters:
            node_id (str): The new unique identifier for the node.
        """
        self.node_id = node_id

    def get_color(self) -> int:
        """Returns the color of the node."""
        return self.color

    def set_color(self, color: int):
        """
        Sets a new color for the node.

        Parameters:
            color (int): The new color for the node.
        """
        self.color = color

    def add_neighbor(self, node: 'Node'):
        """
        Adds a neighboring node to this node's list of neighbors.

        Parameters:
            node (Node): The neighbor node to be added.
        """
        self.neighbors.append(node)

    def get_neighbors(self) -> list['Node']:
        """Returns a list of neighboring nodes."""
        return self.neighbors

    def __str__(self):
        return f"node_id: {self.node_id} - color: {self.color}"


class Graph:
    """
    A class representing a graph, which can be either directed or undirected, consisting
    of nodes and edges between these nodes.

    Attributes:
        nodes (list[Node]): A list of Node instances representing the graph's nodes.
        edges (list[tuple[Node, Node]]): A list of tuples where each tuple represents
            an edge between two nodes.
        is_undirected (bool): A boolean flag indicating whether the graph is undirected.
    """

    def __init__(self, nodes: list['Node'], is_undirected: bool):
        """
        Initializes a new instance of the Graph class.

        Parameters:
            nodes (list[Node]): The nodes of the graph.
            is_undirected (bool): Specifies if the graph is undirected.
        """
        self.nodes = nodes
        self.edges: list[tuple['Node', 'Node']] = []
        self.is_undirected = is_undirected

    def add_edge(self, node1: 'Node', node2: 'Node'):
        """
        Adds an edge between two nodes and updates their neighbors. If the graph is undirected,
        also adds the reverse edge and updates the neighbors accordingly.

        Parameters:
            node1 (Node): The first node in the edge.
            node2 (Node): The second node in the edge.
        """
        self.edges.append((node1, node2))
        node1.add_neighbor(node2)
        if self.is_undirected:
            self.edges.append((node2, node1))
            node2.add_neighbor(node1)

    def get_nodes(self) -> list['Node']:
        """Returns a list of the graph's nodes."""
        return self.nodes

    def get_edges(self) -> list[tuple['Node', 'Node']]:
        """Returns a list of the graph's edges."""
        return self.edges

    def get_is_undirected(self) -> bool:
        return self.is_undirected

    def __str__(self):
        """
        Returns a string representation of the graph. For each node, it shows the node's
        details and a list of its neighbors.

        Returns:
            str: A representation of the graph's connectivity.
        """
        string = ""
        for node in self.nodes:
            string += f"{str(node)} is connected to: | "
            for neighbor in node.get_neighbors():
                string += f"{str(neighbor)} | "
            string = string[:-3]
            string += "\n"
        return string


def setup() -> Graph:
    """Setup the graph according to the exercise and return it."""
    node_A = Node('A', 0)
    node_B = Node('B', 0)
    node_C = Node('C', 0)
    node_D = Node('D', 0)
    node_F = Node('F', 0)
    nodes = [node_A, node_B, node_C, node_D, node_F]
    graph = Graph(nodes=nodes, is_undirected=True)
    graph.add_edge(node_A, node_B)
    graph.add_edge(node_A, node_C)
    graph.add_edge(node_A, node_D)
    graph.add_edge(node_A, node_F)
    graph.add_edge(node_B, node_C)
    graph.add_edge(node_C, node_D)
    graph.add_edge(node_C, node_F)
    graph.add_edge(node_D, node_F)

    return graph


def get_N() -> int:
    """Get N(number of colors) from the user. Ensures that we get a legal input."""
    while True:
        try:
            N = int(input("Please enter the number of colors: "))
            if N < 1:
                print("Please enter a number that is greater than 0.")
            else:
                return N
        except:
            print("Not a valid number!")

#  This is a solution for the coloring graph problem.
if __name__ == "__main__":
    graph = setup()  # Setup graph
    N = get_N()  # get input N - number of colors
    colored_nodes_count = 0  # keep track of number of colored nodes
    is_all_colored = False  # boolean flag that represents if the graph is colored correctly or not
    for color in range(1, N + 1):  # for each color
        for node in graph.get_nodes():  # run over all nodes in the graph
            if node.get_color() == 0:  # if not colored
                is_eligible_for_coloring = True  # a flag for checking if the current node can be colored using the current color
                for neighbor in node.get_neighbors():  # go over all neighbors
                    if neighbor.get_color() == color:  # if there is a neighbor with the current color
                        is_eligible_for_coloring = False  # don't color
                        break  # exit neighbors for loop
                if is_eligible_for_coloring:  # if no neighbors have the current color
                    node.set_color(color)  # change the color of the node to be the current color
                    colored_nodes_count += 1  # add to colored nodes
        if len(graph.get_nodes()) == colored_nodes_count:  # check if all nodes have been colored
            is_all_colored = True
            break  # exit colors for loop
    if is_all_colored:  # if graph was correctly colored using N colors
        print(f"The given graph can be colored with {N} colors!\nGraph: {str(graph)}")
    else:  # if graph can't be colored with N colors
        print(f"The given graph can't be colored with {N} colors!\nGraph: {str(graph)}")


