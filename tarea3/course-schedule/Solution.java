import java.util.ArrayList;
import java.util.List;

/**
 * 207. Course Schedule
 * https://leetcode.com/problems/course-schedule/
 *
 * Modelo (grafo DIRIGIDO, el pensum):
 *   - Vértice: cada materia 0 .. numCourses-1.
 *   - Arco b -> a por cada par [a, b] de prerequisites: "hay que cursar b
 *     antes que a". Se usa un solo convenio en todo el código.
 *   - Se pueden cursar todas las materias si y solo si el grafo es un DAG
 *     (no hay ciclo de prerrequisitos). Un ciclo bloquea a todas sus
 *     materias: ninguna puede ser la primera en cursarse.
 *
 * Algoritmo: orden topológico de Kahn (BFS por grados de entrada).
 *   1. Se construye la lista de adyacencia y el arreglo indegree[] (cuántos
 *      prerrequisitos pendientes tiene cada materia).
 *   2. Se encolan todas las materias con indegree 0: se pueden cursar ya.
 *      Aquí entran también las materias aisladas (sin flechas).
 *   3. Se "cursa" la materia del frente de la cola: se cuenta y se le baja
 *      en 1 el indegree a cada materia que dependía de ella; la que llega a
 *      0 ya tiene todos sus prerrequisitos y se encola.
 *   4. Si al final se cursaron numCourses materias, hay un orden válido
 *      (DAG). Si se cursaron menos, las que sobran nunca llegaron a
 *      indegree 0 porque dependen, directa o indirectamente, de un ciclo.
 *
 * Complejidad (n = numCourses, m = prerequisites.length):
 *   Tiempo:  O(n + m). Construir el grafo recorre los m pares; cada materia
 *            entra y sale de la cola a lo sumo una vez (n) y cada arco se
 *            recorre una sola vez al bajar un indegree (m).
 *   Espacio: O(n + m). Listas de adyacencia (n listas con m arcos en total)
 *            + indegree[] + cola (a lo sumo n materias).
 */
class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> ady = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) ady.add(new ArrayList<>());
        int[] indegree = new int[numCourses];

        for (int[] p : prerequisites) {
            int a = p[0], b = p[1];
            ady.get(b).add(a); // arco b -> a
            indegree[a]++;
        }

        // Cola como arreglo: cada materia se encola a lo sumo una vez.
        int[] cola = new int[numCourses];
        int frente = 0, fin = 0;
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) cola[fin++] = i;
        }

        int cursadas = 0;
        while (frente < fin) {
            int u = cola[frente++];
            cursadas++;
            for (int v : ady.get(u)) {
                if (--indegree[v] == 0) cola[fin++] = v;
            }
        }

        // Menos de numCourses cursadas -> quedó un ciclo.
        return cursadas == numCourses;
    }

    /**
     * Alternativa: DFS de 3 colores (no es la que se envió a LeetCode, se
     * deja para comparar). Blanco = no visitado, gris = en la pila de
     * recursión actual, negro = terminado. Llegar a un nodo GRIS es un arco
     * hacia atrás -> ciclo. Misma complejidad O(n + m) en tiempo y espacio.
     */
    public boolean canFinishDfs(int numCourses, int[][] prerequisites) {
        List<List<Integer>> ady = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) ady.add(new ArrayList<>());
        for (int[] p : prerequisites) ady.get(p[1]).add(p[0]);

        int[] color = new int[numCourses]; // 0 blanco, 1 gris, 2 negro
        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0 && hayCiclo(i, ady, color)) return false;
        }
        return true;
    }

    private boolean hayCiclo(int u, List<List<Integer>> ady, int[] color) {
        color[u] = 1;
        for (int v : ady.get(u)) {
            if (color[v] == 1) return true;
            if (color[v] == 0 && hayCiclo(v, ady, color)) return true;
        }
        color[u] = 2;
        return false;
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.canFinish(2, new int[][]{{1, 0}}));          // true
        System.out.println(sol.canFinish(2, new int[][]{{1, 0}, {0, 1}}));  // false (ciclo)
        System.out.println(sol.canFinish(5, new int[][]{{1, 0}, {2, 1}}));  // true (3 y 4 aisladas)
        System.out.println(sol.canFinish(4, new int[][]{{1, 0}, {2, 1}, {3, 2}, {1, 3}})); // false
        System.out.println(sol.canFinishDfs(4, new int[][]{{1, 0}, {2, 1}, {3, 2}, {1, 3}})); // false
    }
}
