package structures;

import graphs.Edge;
import graphs.IGraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @param <V> Vertex
 * @author Peter Eang
 * @version 1.0
 */
public class DirectedGraph<V> implements IGraph<V>
{
    public static final double THRESHOLD = 0.5;
    public static final double SIZE_INCREASE = 1.5;
    private int[][] matrix;
    private final Stack<Integer> stack = new Stack<>();
    private Bijection<V, Integer> map = new Bijection<>();
    private final int baseSize = 10;
    private int vertices = 0;
    private int edges = 0;

    /**
     * Creates a new directed graph with space for 10 vertices
     */
    public DirectedGraph()
    {
        // new matrix graph created
        matrix = new int[getBaseSize()][getBaseSize()];

        // start stack
        stack.push(0);

        // set counters for later
        setVertices(0);
        setEdges(0);

    }

    /**
     * Used for test case while it says unused?
     * @param initialSize the initial size of the matrix
     */
    public DirectedGraph(int initialSize)
    {
        matrix = new int[initialSize][initialSize];

        stack.push(0);

        setVertices(0);
        setEdges(0);

    }

    @Override
    public boolean addVertex(V vertex)
    {
        // preconditions
        if (containsVertex(vertex))
        {
            return false;
        }

        // once the matrix has passed the threshold for vertices, increase the size of the matrix
        if (vertexSize() > matrix.length * THRESHOLD)
        {
            // create new graph with information from the previous matrix
            // increase size by 0.5
            int[][] graph = new int[(int) (matrix.length * SIZE_INCREASE)][(int) (matrix.length * SIZE_INCREASE)];
            // loop over rows and columns
            for (int i = 0; i < matrix.length; i++)
            {
                for (int j = 0; j < matrix.length; j++)
                {
                    // take data from matrix and add it to new graph
                    graph[i][j] = matrix[i][j];
                }
            }
            // add the new graph to the main matrix to increase size
            matrix = graph;
        }

        int index = stack.pop();

        if (stack.isEmpty()) {
            stack.push(index + 1);
        }

        // add vertex here to map
        map.add(vertex, index);
        vertices++;
        return true;
    }

    @Override
    public boolean addEdge(V source, V destination, int weight)
    {
        // preconditions
        if (!containsVertex(source) || !containsVertex(destination) || containsEdge(source, destination))
        {
            return false;
        }
        // no negative weights allowed
        if (weight <= -1)
        {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        // add weight to edge and increase count
        matrix[map.getValue(source)][map.getValue(destination)] = weight;
        edges++;

        return true;

    }

    @Override
    public int vertexSize()
    {
        return vertices;
    }

    @Override
    public int edgeSize()
    {
        return edges;
    }

    @Override
    public boolean containsVertex(V vertex)
    {
        return map.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(V source, V destination)
    {
        if (!map.containsKey(destination) || !map.containsKey(source))
        {
            return false;
        }
        int compare = matrix[map.getValue(source)][map.getValue(destination)];
        return  compare > 0;

    }

    @Override
    public int edgeWeight(V source, V destination)
    {
        return matrix[map.getValue(source)][map.getValue(destination)];
    }

    @Override
    public Set<V> vertices()
    {
        Set<V> setVertices = new HashSet<>();
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix.length; j++)
            {
                if (matrix[i][j] !=0)
                {
                    setVertices.add(map.getKey(i));
                }
            }
        }
        return setVertices;
    }

    @Override
    public Set<Edge<V>> edges()
    {
        Set<Edge<V>> setEdges = new HashSet<>();
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix.length; j++)
            {
                if (matrix[i][j] != 0)
                {
                    setEdges.add(new Edge<>(map.getKey(i), map.getKey(j), matrix[i][j]));
                }
            }
        }
        return setEdges;
    }

    @Override
    public boolean removeVertex(V vertex)
    {
        if (containsVertex(vertex))
        {
            matrix[map.getValue(vertex)][map.getValue(vertex)] = 0;
            map.removeKey(vertex);
            stack.push(map.getValue(vertex));
            vertices--;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEdge(V source, V destination)
    {
        if (containsEdge(source, destination))
        {
            matrix[map.getValue(source)][map.getValue(destination)] = 0;
            edges--;
            return true;
        }
        return false;
    }

    @Override
    public void clear()
    {
        map = new Bijection<>();
        stack.clear();
        vertices = 0;
        edges = 0;
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix.length; j++)
            {
                matrix[i][j] = 0;
            }
        }
    }

    /**
     * With the given 10 as the base size it is returned
     * @return the given base size
     */
    public int getBaseSize()
    {
        return baseSize;
    }

    /**
     * Set vertices
     * @param vertices point vertices being set
     */
    public void setVertices(int vertices)
    {
        this.vertices = vertices;
    }

    /**
     * Sets edges given.
     * @param edges given edges from graph
     */
    public void setEdges(int edges)
    {
        this.edges = edges;
    }

    @Override
    public String toString()
    {
        return "matrix:" + Arrays.toString(matrix) +
                ", stack=" + stack +
                ", map=" + map +
                ", baseSize=" + baseSize +
                ", vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }
}
