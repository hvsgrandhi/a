#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

int main(int argc, char* argv[]) {
    int rank, size, N;
    int* arr = NULL;
    int* sub_arr = NULL;
    int local_sum = 0, total_sum = 0;

    MPI_Init(&argc, &argv);                         
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);           
    MPI_Comm_size(MPI_COMM_WORLD, &size);           

    if (rank == 0) {
        printf("Enter total number of elements: ");
        scanf("%d", &N);

        // Check if N is divisible by number of processes
        if (N % size != 0) {
            printf("Please enter a number divisible by %d (number of processes).\n", size);
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        // Allocate and read array
        arr = (int*) malloc(N * sizeof(int));
        printf("Enter %d integers:\n", N);
        for (int i = 0; i < N; i++) {
            scanf("%d", &arr[i]);
        }
    }

    // Broadcast N to all processes
    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

    int elements_per_process = N / size;
    sub_arr = (int*) malloc(elements_per_process * sizeof(int));

    // Scatter the input array to all processes
    MPI_Scatter(arr, elements_per_process, MPI_INT, 
                sub_arr, elements_per_process, MPI_INT, 
                0, MPI_COMM_WORLD);

    // Each process computes the sum of its sub-array
    for (int i = 0; i < elements_per_process; i++) {
        local_sum += sub_arr[i];
    }

    printf("Process %d: Partial Sum = %d\n", rank, local_sum);

    // Reduce all local sums to total sum at root
    MPI_Reduce(&local_sum, &total_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        printf("Total Sum = %d\n", total_sum);
        free(arr); // Free only in root
    }

    free(sub_arr); // Free in all processes
    MPI_Finalize();
    return 0;
}
