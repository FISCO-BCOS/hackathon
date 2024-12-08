#ifndef ENCLAVE_T_H__
#define ENCLAVE_T_H__

#include <stdint.h>
#include <wchar.h>
#include <stddef.h>
#include "sgx_edger8r.h" /* for sgx_ocall etc. */

#include "user_types.h"

#include <stdlib.h> /* for size_t */

#define SGX_CAST(type, item) ((type)(item))

#ifdef __cplusplus
extern "C" {
#endif

#ifndef _struct_foo_t
#define _struct_foo_t
typedef struct struct_foo_t {
	uint32_t struct_foo_0;
	uint64_t struct_foo_1;
} struct_foo_t;
#endif

typedef enum enum_foo_t {
	ENUM_FOO_0 = 0,
	ENUM_FOO_1 = 1,
} enum_foo_t;

#ifndef _union_foo_t
#define _union_foo_t
typedef union union_foo_t {
	uint32_t union_foo_0;
	uint32_t union_foo_1;
	uint64_t union_foo_3;
} union_foo_t;
#endif

void ecall_add(int a, int b, int* result);
void ecall_create_wallet(unsigned char* ret_pub_key, unsigned char* ret_priv_key, size_t key_len, int wallet_id, int* ret);
void ecall_deal_transaction(int from_id, int to_id, double amount, char* shuffled_output, size_t shuffled_output_size, char* encrypted_output, size_t encrypted_output_size, int* warning_sign, int* ret);
void ecall_decrypt_transaction_data(const char* encrypted_from, const char* encrypted_id, const char* encrypted_amount, size_t encrypted_size, int* from_id, int* to_id, double* amount, int* ret);
void ecall_encrypt_transaction(int from_id, int to_id, double amount, int* ret);
void ecall_transaction_warning(int from_id, int to_id, double amount, int* warning_sign, int* ret);
void ecall_type_char(char val);
void ecall_type_int(int val);
void ecall_type_float(float val);
void ecall_type_double(double val);
void ecall_type_size_t(size_t val);
void ecall_type_wchar_t(wchar_t val);
void ecall_type_struct(struct struct_foo_t val);
void ecall_type_enum_union(enum enum_foo_t val1, union union_foo_t* val2);
size_t ecall_pointer_user_check(void* val, size_t sz);
void ecall_pointer_in(int* val);
void ecall_pointer_out(int* val);
void ecall_pointer_in_out(int* val);
void ecall_pointer_string(char* str);
void ecall_pointer_string_const(const char* str);
void ecall_pointer_size(void* ptr, size_t len);
void ecall_pointer_count(int* arr, size_t cnt);
void ecall_pointer_isptr_readonly(buffer_t buf, size_t len);
void ocall_pointer_attr(void);
void ecall_array_user_check(int arr[4]);
void ecall_array_in(int arr[4]);
void ecall_array_out(int arr[4]);
void ecall_array_in_out(int arr[4]);
void ecall_array_isary(array_t arr);
void ecall_function_public(void);
int ecall_function_private(void);
void ecall_malloc_free(void);
void ecall_sgx_cpuid(int cpuinfo[4], int leaf);
void ecall_exception(void);
void ecall_map(void);
size_t ecall_increase_counter(void);
void ecall_producer(void);
void ecall_consumer(void);

sgx_status_t SGX_CDECL ocall_print_string(const char* str);
sgx_status_t SGX_CDECL ocall_file_exists(const char* filename, int* exists);
sgx_status_t SGX_CDECL ocall_read_file(const char* filename, char* buffer, size_t buffer_size, int* ret);
sgx_status_t SGX_CDECL ocall_write_file(const char* filename, const char* data, size_t data_size, int write_mode, int* ret);
sgx_status_t SGX_CDECL ocall_pointer_user_check(int* val);
sgx_status_t SGX_CDECL ocall_pointer_in(int* val);
sgx_status_t SGX_CDECL ocall_pointer_out(int* val);
sgx_status_t SGX_CDECL ocall_pointer_in_out(int* val);
sgx_status_t SGX_CDECL ocall_function_allow(void);
sgx_status_t SGX_CDECL sgx_oc_cpuidex(int cpuinfo[4], int leaf, int subleaf);
sgx_status_t SGX_CDECL sgx_thread_wait_untrusted_event_ocall(int* retval, const void* self);
sgx_status_t SGX_CDECL sgx_thread_set_untrusted_event_ocall(int* retval, const void* waiter);
sgx_status_t SGX_CDECL sgx_thread_setwait_untrusted_events_ocall(int* retval, const void* waiter, const void* self);
sgx_status_t SGX_CDECL sgx_thread_set_multiple_untrusted_events_ocall(int* retval, const void** waiters, size_t total);

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif
