
#include <stdio.h>
#include <mpi.h>

int main(int argc, char* argv[]) {
    int rank, size;
    int N = 8;  // Total number of elements in the array
    int arr[] = {10, 20, 30, 40, 50, 60, 70, 80}; // Example array
    int local_sum = 0, total_sum = 0;

    MPI_Init(&argc, &argv);                         // Initialize MPI
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);           // Get current process ID
    MPI_Comm_size(MPI_COMM_WORLD, &size);           // Get total number of processes

    int elements_per_process = N / size;
    int start = rank * elements_per_process;
    int end = start + elements_per_process;

    // Handle remainder if N is not divisible by size
    if (rank == size - 1) {
        end = N;
    }

    // Each process computes sum of its part
    for (int i = start; i < end; i++) {
        local_sum += arr[i];
    }

    printf("Process %d: Partial Sum = %d\n", rank, local_sum);

    // Reduce all local sums into total_sum at root process
    MPI_Reduce(&local_sum, &total_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        printf("Total Sum = %d\n", total_sum);
    }

    MPI_Finalize();   // Finalize MPI
    return 0;
}
