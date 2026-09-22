/**
 * 547. Number of Provinces
 * https://leetcode.com/problems/number-of-provinces/
 *
 * Modelo (grafo NO dirigido):
 *   - Vértice: cada ciudad 0 .. n-1.
 *   - Arista {i, j}: si i != j e isConnected[i][j] == 1. La matriz es
 *     simétrica (la conexión es recíproca), por eso el grafo es no dirigido.
 *   - La diagonal isConnected[i][i] = 1 NO es arista hacia otra ciudad.
 *   - Provincia = componente conexa. La respuesta es cuántas componentes hay.
 *
 * Algoritmo: DFS para contar componentes conexas.
 *   Se recorren las ciudades en orden. Cada vez que aparece una ciudad NO
 *   visitada, se descubrió una provincia nueva: se suma 1 y se lanza un DFS
 *   desde ella que marca como visitadas todas las ciudades alcanzables
 *   (directa o indirectamente). Esas ciudades ya no vuelven a iniciar un
 *   recuento, así que cada componente se cuenta exactamente una vez.
 *   Una ciudad aislada (fila con un solo 1, el de la diagonal) forma su
 *   propia provincia de tamaño 1.
 *
 * El DFS se hace con pila explícita (no recursiva) para no depender del
 * tamaño de la pila de llamadas.
 *
 * Complejidad (n = número de ciudades):
 *   Tiempo:  Θ(n²). La entrada es una matriz de adyacencia: cada ciudad se
 *            saca de la pila una sola vez y al sacarla se recorre su fila
 *            completa (n celdas) -> n · n celdas en total. Sobre una lista
 *            de adyacencia el DFS sería Θ(n + m), pero leer la matriz ya
 *            cuesta Θ(n²).
 *   Espacio: O(n) extra -> arreglo visitado[] + pila (a lo sumo n ciudades).
 */
class Solution {
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visitado = new boolean[n];
        int[] pila = new int[n];
        int provincias = 0;

        for (int inicio = 0; inicio < n; inicio++) {
            if (visitado[inicio]) continue;

            // Ciudad no visitada -> nueva componente conexa.
            provincias++;
            int tope = 0;
            pila[tope++] = inicio;
            visitado[inicio] = true;

            // DFS: marca todo el grupo de "inicio".
            while (tope > 0) {
                int u = pila[--tope];
                for (int v = 0; v < n; v++) {
                    // v != u excluye la diagonal (no es una arista).
                    if (v != u && isConnected[u][v] == 1 && !visitado[v]) {
                        visitado[v] = true; // se marca al apilar: cada ciudad entra una sola vez
                        pila[tope++] = v;
                    }
                }
            }
        }
        return provincias;
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.findCircleNum(new int[][]{
            {1, 1, 0}, {1, 1, 0}, {0, 0, 1}})); // 2
        System.out.println(sol.findCircleNum(new int[][]{
            {1, 0, 0}, {0, 1, 0}, {0, 0, 1}})); // 3
        System.out.println(sol.findCircleNum(new int[][]{
            {1, 0, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1}, {1, 0, 1, 1}})); // 1 (conexión indirecta 0-3-2-1)
    }
}
