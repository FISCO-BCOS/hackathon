#include "Enclave_t.h"

#include "sgx_trts.h" /* for sgx_ocalloc, sgx_is_outside_enclave */
#include "sgx_lfence.h" /* for sgx_lfence */

#include <errno.h>
#include <mbusafecrt.h> /* for memcpy_s etc */
#include <stdlib.h> /* for malloc/free etc */

#define CHECK_REF_POINTER(ptr, siz) do {	\
	if (!(ptr) || ! sgx_is_outside_enclave((ptr), (siz)))	\
		return SGX_ERROR_INVALID_PARAMETER;\
} while (0)

#define CHECK_UNIQUE_POINTER(ptr, siz) do {	\
	if ((ptr) && ! sgx_is_outside_enclave((ptr), (siz)))	\
		return SGX_ERROR_INVALID_PARAMETER;\
} while (0)

#define CHECK_ENCLAVE_POINTER(ptr, siz) do {	\
	if ((ptr) && ! sgx_is_within_enclave((ptr), (siz)))	\
		return SGX_ERROR_INVALID_PARAMETER;\
} while (0)

#define ADD_ASSIGN_OVERFLOW(a, b) (	\
	((a) += (b)) < (b)	\
)


typedef struct ms_ecall_add_t {
	int ms_a;
	int ms_b;
	int* ms_result;
} ms_ecall_add_t;

typedef struct ms_ecall_create_wallet_t {
	unsigned char* ms_ret_pub_key;
	unsigned char* ms_ret_priv_key;
	size_t ms_key_len;
	int ms_wallet_id;
	int* ms_ret;
} ms_ecall_create_wallet_t;

typedef struct ms_ecall_deal_transaction_t {
	int ms_from_id;
	int ms_to_id;
	double ms_amount;
	char* ms_shuffled_output;
	size_t ms_shuffled_output_size;
	char* ms_encrypted_output;
	size_t ms_encrypted_output_size;
	int* ms_warning_sign;
	int* ms_ret;
} ms_ecall_deal_transaction_t;

typedef struct ms_ecall_decrypt_transaction_data_t {
	const char* ms_encrypted_from;
	const char* ms_encrypted_id;
	const char* ms_encrypted_amount;
	size_t ms_encrypted_size;
	int* ms_from_id;
	int* ms_to_id;
	double* ms_amount;
	int* ms_ret;
} ms_ecall_decrypt_transaction_data_t;

typedef struct ms_ecall_encrypt_transaction_t {
	int ms_from_id;
	int ms_to_id;
	double ms_amount;
	int* ms_ret;
} ms_ecall_encrypt_transaction_t;

typedef struct ms_ecall_transaction_warning_t {
	int ms_from_id;
	int ms_to_id;
	double ms_amount;
	int* ms_warning_sign;
	int* ms_ret;
} ms_ecall_transaction_warning_t;

typedef struct ms_ecall_type_char_t {
	char ms_val;
} ms_ecall_type_char_t;

typedef struct ms_ecall_type_int_t {
	int ms_val;
} ms_ecall_type_int_t;

typedef struct ms_ecall_type_float_t {
	float ms_val;
} ms_ecall_type_float_t;

typedef struct ms_ecall_type_double_t {
	double ms_val;
} ms_ecall_type_double_t;

typedef struct ms_ecall_type_size_t_t {
	size_t ms_val;
} ms_ecall_type_size_t_t;

typedef struct ms_ecall_type_wchar_t_t {
	wchar_t ms_val;
} ms_ecall_type_wchar_t_t;

typedef struct ms_ecall_type_struct_t {
	struct struct_foo_t ms_val;
} ms_ecall_type_struct_t;

typedef struct ms_ecall_type_enum_union_t {
	enum enum_foo_t ms_val1;
	union union_foo_t* ms_val2;
} ms_ecall_type_enum_union_t;

typedef struct ms_ecall_pointer_user_check_t {
	size_t ms_retval;
	void* ms_val;
	size_t ms_sz;
} ms_ecall_pointer_user_check_t;

typedef struct ms_ecall_pointer_in_t {
	int* ms_val;
} ms_ecall_pointer_in_t;

typedef struct ms_ecall_pointer_out_t {
	int* ms_val;
} ms_ecall_pointer_out_t;

typedef struct ms_ecall_pointer_in_out_t {
	int* ms_val;
} ms_ecall_pointer_in_out_t;

typedef struct ms_ecall_pointer_string_t {
	char* ms_str;
	size_t ms_str_len;
} ms_ecall_pointer_string_t;

typedef struct ms_ecall_pointer_string_const_t {
	const char* ms_str;
	size_t ms_str_len;
} ms_ecall_pointer_string_const_t;

typedef struct ms_ecall_pointer_size_t {
	void* ms_ptr;
	size_t ms_len;
} ms_ecall_pointer_size_t;

typedef struct ms_ecall_pointer_count_t {
	int* ms_arr;
	size_t ms_cnt;
} ms_ecall_pointer_count_t;

typedef struct ms_ecall_pointer_isptr_readonly_t {
	buffer_t ms_buf;
	size_t ms_len;
} ms_ecall_pointer_isptr_readonly_t;

typedef struct ms_ecall_array_user_check_t {
	int* ms_arr;
} ms_ecall_array_user_check_t;

typedef struct ms_ecall_array_in_t {
	int* ms_arr;
} ms_ecall_array_in_t;

typedef struct ms_ecall_array_out_t {
	int* ms_arr;
} ms_ecall_array_out_t;

typedef struct ms_ecall_array_in_out_t {
	int* ms_arr;
} ms_ecall_array_in_out_t;

typedef struct ms_ecall_array_isary_t {
	array_t*  ms_arr;
} ms_ecall_array_isary_t;

typedef struct ms_ecall_function_private_t {
	int ms_retval;
} ms_ecall_function_private_t;

typedef struct ms_ecall_sgx_cpuid_t {
	int* ms_cpuinfo;
	int ms_leaf;
} ms_ecall_sgx_cpuid_t;

typedef struct ms_ecall_increase_counter_t {
	size_t ms_retval;
} ms_ecall_increase_counter_t;

typedef struct ms_ocall_print_string_t {
	const char* ms_str;
} ms_ocall_print_string_t;

typedef struct ms_ocall_file_exists_t {
	const char* ms_filename;
	int* ms_exists;
} ms_ocall_file_exists_t;

typedef struct ms_ocall_read_file_t {
	const char* ms_filename;
	char* ms_buffer;
	size_t ms_buffer_size;
	int* ms_ret;
} ms_ocall_read_file_t;

typedef struct ms_ocall_write_file_t {
	const char* ms_filename;
	const char* ms_data;
	size_t ms_data_size;
	int ms_write_mode;
	int* ms_ret;
} ms_ocall_write_file_t;

typedef struct ms_ocall_pointer_user_check_t {
	int* ms_val;
} ms_ocall_pointer_user_check_t;

typedef struct ms_ocall_pointer_in_t {
	int* ms_val;
} ms_ocall_pointer_in_t;

typedef struct ms_ocall_pointer_out_t {
	int* ms_val;
} ms_ocall_pointer_out_t;

typedef struct ms_ocall_pointer_in_out_t {
	int* ms_val;
} ms_ocall_pointer_in_out_t;

typedef struct ms_sgx_oc_cpuidex_t {
	int* ms_cpuinfo;
	int ms_leaf;
	int ms_subleaf;
} ms_sgx_oc_cpuidex_t;

typedef struct ms_sgx_thread_wait_untrusted_event_ocall_t {
	int ms_retval;
	const void* ms_self;
} ms_sgx_thread_wait_untrusted_event_ocall_t;

typedef struct ms_sgx_thread_set_untrusted_event_ocall_t {
	int ms_retval;
	const void* ms_waiter;
} ms_sgx_thread_set_untrusted_event_ocall_t;

typedef struct ms_sgx_thread_setwait_untrusted_events_ocall_t {
	int ms_retval;
	const void* ms_waiter;
	const void* ms_self;
} ms_sgx_thread_setwait_untrusted_events_ocall_t;

typedef struct ms_sgx_thread_set_multiple_untrusted_events_ocall_t {
	int ms_retval;
	const void** ms_waiters;
	size_t ms_total;
} ms_sgx_thread_set_multiple_untrusted_events_ocall_t;

static sgx_status_t SGX_CDECL sgx_ecall_add(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_add_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_add_t* ms = SGX_CAST(ms_ecall_add_t*, pms);
	ms_ecall_add_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_add_t), ms, sizeof(ms_ecall_add_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_result = __in_ms.ms_result;
	size_t _len_result = sizeof(int);
	int* _in_result = NULL;

	CHECK_UNIQUE_POINTER(_tmp_result, _len_result);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_result != NULL && _len_result != 0) {
		if ( _len_result % sizeof(*_tmp_result) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_result = (int*)malloc(_len_result)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_result, 0, _len_result);
	}
	ecall_add(__in_ms.ms_a, __in_ms.ms_b, _in_result);
	if (_in_result) {
		if (memcpy_verw_s(_tmp_result, _len_result, _in_result, _len_result)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_result) free(_in_result);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_create_wallet(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_create_wallet_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_create_wallet_t* ms = SGX_CAST(ms_ecall_create_wallet_t*, pms);
	ms_ecall_create_wallet_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_create_wallet_t), ms, sizeof(ms_ecall_create_wallet_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	unsigned char* _tmp_ret_pub_key = __in_ms.ms_ret_pub_key;
	size_t _tmp_key_len = __in_ms.ms_key_len;
	size_t _len_ret_pub_key = _tmp_key_len;
	unsigned char* _in_ret_pub_key = NULL;
	unsigned char* _tmp_ret_priv_key = __in_ms.ms_ret_priv_key;
	size_t _len_ret_priv_key = _tmp_key_len;
	unsigned char* _in_ret_priv_key = NULL;
	int* _tmp_ret = __in_ms.ms_ret;
	size_t _len_ret = sizeof(int);
	int* _in_ret = NULL;

	CHECK_UNIQUE_POINTER(_tmp_ret_pub_key, _len_ret_pub_key);
	CHECK_UNIQUE_POINTER(_tmp_ret_priv_key, _len_ret_priv_key);
	CHECK_UNIQUE_POINTER(_tmp_ret, _len_ret);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_ret_pub_key != NULL && _len_ret_pub_key != 0) {
		if ( _len_ret_pub_key % sizeof(*_tmp_ret_pub_key) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret_pub_key = (unsigned char*)malloc(_len_ret_pub_key)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret_pub_key, 0, _len_ret_pub_key);
	}
	if (_tmp_ret_priv_key != NULL && _len_ret_priv_key != 0) {
		if ( _len_ret_priv_key % sizeof(*_tmp_ret_priv_key) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret_priv_key = (unsigned char*)malloc(_len_ret_priv_key)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret_priv_key, 0, _len_ret_priv_key);
	}
	if (_tmp_ret != NULL && _len_ret != 0) {
		if ( _len_ret % sizeof(*_tmp_ret) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret = (int*)malloc(_len_ret)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret, 0, _len_ret);
	}
	ecall_create_wallet(_in_ret_pub_key, _in_ret_priv_key, _tmp_key_len, __in_ms.ms_wallet_id, _in_ret);
	if (_in_ret_pub_key) {
		if (memcpy_verw_s(_tmp_ret_pub_key, _len_ret_pub_key, _in_ret_pub_key, _len_ret_pub_key)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_ret_priv_key) {
		if (memcpy_verw_s(_tmp_ret_priv_key, _len_ret_priv_key, _in_ret_priv_key, _len_ret_priv_key)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_ret) {
		if (memcpy_verw_s(_tmp_ret, _len_ret, _in_ret, _len_ret)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_ret_pub_key) free(_in_ret_pub_key);
	if (_in_ret_priv_key) free(_in_ret_priv_key);
	if (_in_ret) free(_in_ret);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_deal_transaction(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_deal_transaction_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_deal_transaction_t* ms = SGX_CAST(ms_ecall_deal_transaction_t*, pms);
	ms_ecall_deal_transaction_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_deal_transaction_t), ms, sizeof(ms_ecall_deal_transaction_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	char* _tmp_shuffled_output = __in_ms.ms_shuffled_output;
	size_t _tmp_shuffled_output_size = __in_ms.ms_shuffled_output_size;
	size_t _len_shuffled_output = _tmp_shuffled_output_size;
	char* _in_shuffled_output = NULL;
	char* _tmp_encrypted_output = __in_ms.ms_encrypted_output;
	size_t _tmp_encrypted_output_size = __in_ms.ms_encrypted_output_size;
	size_t _len_encrypted_output = _tmp_encrypted_output_size;
	char* _in_encrypted_output = NULL;
	int* _tmp_warning_sign = __in_ms.ms_warning_sign;
	size_t _len_warning_sign = sizeof(int);
	int* _in_warning_sign = NULL;
	int* _tmp_ret = __in_ms.ms_ret;
	size_t _len_ret = sizeof(int);
	int* _in_ret = NULL;

	CHECK_UNIQUE_POINTER(_tmp_shuffled_output, _len_shuffled_output);
	CHECK_UNIQUE_POINTER(_tmp_encrypted_output, _len_encrypted_output);
	CHECK_UNIQUE_POINTER(_tmp_warning_sign, _len_warning_sign);
	CHECK_UNIQUE_POINTER(_tmp_ret, _len_ret);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_shuffled_output != NULL && _len_shuffled_output != 0) {
		if ( _len_shuffled_output % sizeof(*_tmp_shuffled_output) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_shuffled_output = (char*)malloc(_len_shuffled_output)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_shuffled_output, 0, _len_shuffled_output);
	}
	if (_tmp_encrypted_output != NULL && _len_encrypted_output != 0) {
		if ( _len_encrypted_output % sizeof(*_tmp_encrypted_output) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_encrypted_output = (char*)malloc(_len_encrypted_output)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_encrypted_output, 0, _len_encrypted_output);
	}
	if (_tmp_warning_sign != NULL && _len_warning_sign != 0) {
		if ( _len_warning_sign % sizeof(*_tmp_warning_sign) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_warning_sign = (int*)malloc(_len_warning_sign)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_warning_sign, 0, _len_warning_sign);
	}
	if (_tmp_ret != NULL && _len_ret != 0) {
		if ( _len_ret % sizeof(*_tmp_ret) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret = (int*)malloc(_len_ret)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret, 0, _len_ret);
	}
	ecall_deal_transaction(__in_ms.ms_from_id, __in_ms.ms_to_id, __in_ms.ms_amount, _in_shuffled_output, _tmp_shuffled_output_size, _in_encrypted_output, _tmp_encrypted_output_size, _in_warning_sign, _in_ret);
	if (_in_shuffled_output) {
		if (memcpy_verw_s(_tmp_shuffled_output, _len_shuffled_output, _in_shuffled_output, _len_shuffled_output)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_encrypted_output) {
		if (memcpy_verw_s(_tmp_encrypted_output, _len_encrypted_output, _in_encrypted_output, _len_encrypted_output)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_warning_sign) {
		if (memcpy_verw_s(_tmp_warning_sign, _len_warning_sign, _in_warning_sign, _len_warning_sign)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_ret) {
		if (memcpy_verw_s(_tmp_ret, _len_ret, _in_ret, _len_ret)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_shuffled_output) free(_in_shuffled_output);
	if (_in_encrypted_output) free(_in_encrypted_output);
	if (_in_warning_sign) free(_in_warning_sign);
	if (_in_ret) free(_in_ret);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_decrypt_transaction_data(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_decrypt_transaction_data_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_decrypt_transaction_data_t* ms = SGX_CAST(ms_ecall_decrypt_transaction_data_t*, pms);
	ms_ecall_decrypt_transaction_data_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_decrypt_transaction_data_t), ms, sizeof(ms_ecall_decrypt_transaction_data_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	const char* _tmp_encrypted_from = __in_ms.ms_encrypted_from;
	size_t _tmp_encrypted_size = __in_ms.ms_encrypted_size;
	size_t _len_encrypted_from = _tmp_encrypted_size;
	char* _in_encrypted_from = NULL;
	const char* _tmp_encrypted_id = __in_ms.ms_encrypted_id;
	size_t _len_encrypted_id = _tmp_encrypted_size;
	char* _in_encrypted_id = NULL;
	const char* _tmp_encrypted_amount = __in_ms.ms_encrypted_amount;
	size_t _len_encrypted_amount = _tmp_encrypted_size;
	char* _in_encrypted_amount = NULL;
	int* _tmp_from_id = __in_ms.ms_from_id;
	size_t _len_from_id = sizeof(int);
	int* _in_from_id = NULL;
	int* _tmp_to_id = __in_ms.ms_to_id;
	size_t _len_to_id = sizeof(int);
	int* _in_to_id = NULL;
	double* _tmp_amount = __in_ms.ms_amount;
	size_t _len_amount = sizeof(double);
	double* _in_amount = NULL;
	int* _tmp_ret = __in_ms.ms_ret;
	size_t _len_ret = sizeof(int);
	int* _in_ret = NULL;

	CHECK_UNIQUE_POINTER(_tmp_encrypted_from, _len_encrypted_from);
	CHECK_UNIQUE_POINTER(_tmp_encrypted_id, _len_encrypted_id);
	CHECK_UNIQUE_POINTER(_tmp_encrypted_amount, _len_encrypted_amount);
	CHECK_UNIQUE_POINTER(_tmp_from_id, _len_from_id);
	CHECK_UNIQUE_POINTER(_tmp_to_id, _len_to_id);
	CHECK_UNIQUE_POINTER(_tmp_amount, _len_amount);
	CHECK_UNIQUE_POINTER(_tmp_ret, _len_ret);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_encrypted_from != NULL && _len_encrypted_from != 0) {
		if ( _len_encrypted_from % sizeof(*_tmp_encrypted_from) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_encrypted_from = (char*)malloc(_len_encrypted_from);
		if (_in_encrypted_from == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_encrypted_from, _len_encrypted_from, _tmp_encrypted_from, _len_encrypted_from)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	if (_tmp_encrypted_id != NULL && _len_encrypted_id != 0) {
		if ( _len_encrypted_id % sizeof(*_tmp_encrypted_id) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_encrypted_id = (char*)malloc(_len_encrypted_id);
		if (_in_encrypted_id == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_encrypted_id, _len_encrypted_id, _tmp_encrypted_id, _len_encrypted_id)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	if (_tmp_encrypted_amount != NULL && _len_encrypted_amount != 0) {
		if ( _len_encrypted_amount % sizeof(*_tmp_encrypted_amount) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_encrypted_amount = (char*)malloc(_len_encrypted_amount);
		if (_in_encrypted_amount == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_encrypted_amount, _len_encrypted_amount, _tmp_encrypted_amount, _len_encrypted_amount)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	if (_tmp_from_id != NULL && _len_from_id != 0) {
		if ( _len_from_id % sizeof(*_tmp_from_id) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_from_id = (int*)malloc(_len_from_id)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_from_id, 0, _len_from_id);
	}
	if (_tmp_to_id != NULL && _len_to_id != 0) {
		if ( _len_to_id % sizeof(*_tmp_to_id) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_to_id = (int*)malloc(_len_to_id)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_to_id, 0, _len_to_id);
	}
	if (_tmp_amount != NULL && _len_amount != 0) {
		if ( _len_amount % sizeof(*_tmp_amount) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_amount = (double*)malloc(_len_amount)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_amount, 0, _len_amount);
	}
	if (_tmp_ret != NULL && _len_ret != 0) {
		if ( _len_ret % sizeof(*_tmp_ret) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret = (int*)malloc(_len_ret)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret, 0, _len_ret);
	}
	ecall_decrypt_transaction_data((const char*)_in_encrypted_from, (const char*)_in_encrypted_id, (const char*)_in_encrypted_amount, _tmp_encrypted_size, _in_from_id, _in_to_id, _in_amount, _in_ret);
	if (_in_from_id) {
		if (memcpy_verw_s(_tmp_from_id, _len_from_id, _in_from_id, _len_from_id)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_to_id) {
		if (memcpy_verw_s(_tmp_to_id, _len_to_id, _in_to_id, _len_to_id)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_amount) {
		if (memcpy_verw_s(_tmp_amount, _len_amount, _in_amount, _len_amount)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_ret) {
		if (memcpy_verw_s(_tmp_ret, _len_ret, _in_ret, _len_ret)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_encrypted_from) free(_in_encrypted_from);
	if (_in_encrypted_id) free(_in_encrypted_id);
	if (_in_encrypted_amount) free(_in_encrypted_amount);
	if (_in_from_id) free(_in_from_id);
	if (_in_to_id) free(_in_to_id);
	if (_in_amount) free(_in_amount);
	if (_in_ret) free(_in_ret);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_encrypt_transaction(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_encrypt_transaction_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_encrypt_transaction_t* ms = SGX_CAST(ms_ecall_encrypt_transaction_t*, pms);
	ms_ecall_encrypt_transaction_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_encrypt_transaction_t), ms, sizeof(ms_ecall_encrypt_transaction_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_ret = __in_ms.ms_ret;
	size_t _len_ret = sizeof(int);
	int* _in_ret = NULL;

	CHECK_UNIQUE_POINTER(_tmp_ret, _len_ret);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_ret != NULL && _len_ret != 0) {
		if ( _len_ret % sizeof(*_tmp_ret) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret = (int*)malloc(_len_ret)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret, 0, _len_ret);
	}
	ecall_encrypt_transaction(__in_ms.ms_from_id, __in_ms.ms_to_id, __in_ms.ms_amount, _in_ret);
	if (_in_ret) {
		if (memcpy_verw_s(_tmp_ret, _len_ret, _in_ret, _len_ret)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_ret) free(_in_ret);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_transaction_warning(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_transaction_warning_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_transaction_warning_t* ms = SGX_CAST(ms_ecall_transaction_warning_t*, pms);
	ms_ecall_transaction_warning_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_transaction_warning_t), ms, sizeof(ms_ecall_transaction_warning_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_warning_sign = __in_ms.ms_warning_sign;
	size_t _len_warning_sign = sizeof(int);
	int* _in_warning_sign = NULL;
	int* _tmp_ret = __in_ms.ms_ret;
	size_t _len_ret = sizeof(int);
	int* _in_ret = NULL;

	CHECK_UNIQUE_POINTER(_tmp_warning_sign, _len_warning_sign);
	CHECK_UNIQUE_POINTER(_tmp_ret, _len_ret);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_warning_sign != NULL && _len_warning_sign != 0) {
		if ( _len_warning_sign % sizeof(*_tmp_warning_sign) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_warning_sign = (int*)malloc(_len_warning_sign)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_warning_sign, 0, _len_warning_sign);
	}
	if (_tmp_ret != NULL && _len_ret != 0) {
		if ( _len_ret % sizeof(*_tmp_ret) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_ret = (int*)malloc(_len_ret)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_ret, 0, _len_ret);
	}
	ecall_transaction_warning(__in_ms.ms_from_id, __in_ms.ms_to_id, __in_ms.ms_amount, _in_warning_sign, _in_ret);
	if (_in_warning_sign) {
		if (memcpy_verw_s(_tmp_warning_sign, _len_warning_sign, _in_warning_sign, _len_warning_sign)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	if (_in_ret) {
		if (memcpy_verw_s(_tmp_ret, _len_ret, _in_ret, _len_ret)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_warning_sign) free(_in_warning_sign);
	if (_in_ret) free(_in_ret);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_char(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_char_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_char_t* ms = SGX_CAST(ms_ecall_type_char_t*, pms);
	ms_ecall_type_char_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_char_t), ms, sizeof(ms_ecall_type_char_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_char(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_int(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_int_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_int_t* ms = SGX_CAST(ms_ecall_type_int_t*, pms);
	ms_ecall_type_int_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_int_t), ms, sizeof(ms_ecall_type_int_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_int(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_float(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_float_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_float_t* ms = SGX_CAST(ms_ecall_type_float_t*, pms);
	ms_ecall_type_float_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_float_t), ms, sizeof(ms_ecall_type_float_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_float(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_double(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_double_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_double_t* ms = SGX_CAST(ms_ecall_type_double_t*, pms);
	ms_ecall_type_double_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_double_t), ms, sizeof(ms_ecall_type_double_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_double(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_size_t(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_size_t_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_size_t_t* ms = SGX_CAST(ms_ecall_type_size_t_t*, pms);
	ms_ecall_type_size_t_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_size_t_t), ms, sizeof(ms_ecall_type_size_t_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_size_t(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_wchar_t(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_wchar_t_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_wchar_t_t* ms = SGX_CAST(ms_ecall_type_wchar_t_t*, pms);
	ms_ecall_type_wchar_t_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_wchar_t_t), ms, sizeof(ms_ecall_type_wchar_t_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_wchar_t(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_struct(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_struct_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_struct_t* ms = SGX_CAST(ms_ecall_type_struct_t*, pms);
	ms_ecall_type_struct_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_struct_t), ms, sizeof(ms_ecall_type_struct_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_type_struct(__in_ms.ms_val);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_type_enum_union(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_type_enum_union_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_type_enum_union_t* ms = SGX_CAST(ms_ecall_type_enum_union_t*, pms);
	ms_ecall_type_enum_union_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_type_enum_union_t), ms, sizeof(ms_ecall_type_enum_union_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	union union_foo_t* _tmp_val2 = __in_ms.ms_val2;


	ecall_type_enum_union(__in_ms.ms_val1, _tmp_val2);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_user_check(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_user_check_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_user_check_t* ms = SGX_CAST(ms_ecall_pointer_user_check_t*, pms);
	ms_ecall_pointer_user_check_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_user_check_t), ms, sizeof(ms_ecall_pointer_user_check_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	void* _tmp_val = __in_ms.ms_val;
	size_t _in_retval;


	_in_retval = ecall_pointer_user_check(_tmp_val, __in_ms.ms_sz);
	if (memcpy_verw_s(&ms->ms_retval, sizeof(ms->ms_retval), &_in_retval, sizeof(_in_retval))) {
		status = SGX_ERROR_UNEXPECTED;
		goto err;
	}

err:
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_in(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_in_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_in_t* ms = SGX_CAST(ms_ecall_pointer_in_t*, pms);
	ms_ecall_pointer_in_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_in_t), ms, sizeof(ms_ecall_pointer_in_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_val = __in_ms.ms_val;
	size_t _len_val = sizeof(int);
	int* _in_val = NULL;

	CHECK_UNIQUE_POINTER(_tmp_val, _len_val);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_val != NULL && _len_val != 0) {
		if ( _len_val % sizeof(*_tmp_val) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_val = (int*)malloc(_len_val);
		if (_in_val == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_val, _len_val, _tmp_val, _len_val)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_pointer_in(_in_val);

err:
	if (_in_val) free(_in_val);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_out(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_out_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_out_t* ms = SGX_CAST(ms_ecall_pointer_out_t*, pms);
	ms_ecall_pointer_out_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_out_t), ms, sizeof(ms_ecall_pointer_out_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_val = __in_ms.ms_val;
	size_t _len_val = sizeof(int);
	int* _in_val = NULL;

	CHECK_UNIQUE_POINTER(_tmp_val, _len_val);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_val != NULL && _len_val != 0) {
		if ( _len_val % sizeof(*_tmp_val) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_val = (int*)malloc(_len_val)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_val, 0, _len_val);
	}
	ecall_pointer_out(_in_val);
	if (_in_val) {
		if (memcpy_verw_s(_tmp_val, _len_val, _in_val, _len_val)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_val) free(_in_val);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_in_out(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_in_out_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_in_out_t* ms = SGX_CAST(ms_ecall_pointer_in_out_t*, pms);
	ms_ecall_pointer_in_out_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_in_out_t), ms, sizeof(ms_ecall_pointer_in_out_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_val = __in_ms.ms_val;
	size_t _len_val = sizeof(int);
	int* _in_val = NULL;

	CHECK_UNIQUE_POINTER(_tmp_val, _len_val);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_val != NULL && _len_val != 0) {
		if ( _len_val % sizeof(*_tmp_val) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_val = (int*)malloc(_len_val);
		if (_in_val == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_val, _len_val, _tmp_val, _len_val)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_pointer_in_out(_in_val);
	if (_in_val) {
		if (memcpy_verw_s(_tmp_val, _len_val, _in_val, _len_val)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_val) free(_in_val);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_string(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_string_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_string_t* ms = SGX_CAST(ms_ecall_pointer_string_t*, pms);
	ms_ecall_pointer_string_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_string_t), ms, sizeof(ms_ecall_pointer_string_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	char* _tmp_str = __in_ms.ms_str;
	size_t _len_str = __in_ms.ms_str_len ;
	char* _in_str = NULL;

	CHECK_UNIQUE_POINTER(_tmp_str, _len_str);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_str != NULL && _len_str != 0) {
		_in_str = (char*)malloc(_len_str);
		if (_in_str == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_str, _len_str, _tmp_str, _len_str)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

		_in_str[_len_str - 1] = '\0';
		if (_len_str != strlen(_in_str) + 1)
		{
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	ecall_pointer_string(_in_str);
	if (_in_str)
	{
		_in_str[_len_str - 1] = '\0';
		_len_str = strlen(_in_str) + 1;
		if (memcpy_verw_s((void*)_tmp_str, _len_str, _in_str, _len_str)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_str) free(_in_str);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_string_const(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_string_const_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_string_const_t* ms = SGX_CAST(ms_ecall_pointer_string_const_t*, pms);
	ms_ecall_pointer_string_const_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_string_const_t), ms, sizeof(ms_ecall_pointer_string_const_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	const char* _tmp_str = __in_ms.ms_str;
	size_t _len_str = __in_ms.ms_str_len ;
	char* _in_str = NULL;

	CHECK_UNIQUE_POINTER(_tmp_str, _len_str);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_str != NULL && _len_str != 0) {
		_in_str = (char*)malloc(_len_str);
		if (_in_str == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_str, _len_str, _tmp_str, _len_str)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

		_in_str[_len_str - 1] = '\0';
		if (_len_str != strlen(_in_str) + 1)
		{
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}
	ecall_pointer_string_const((const char*)_in_str);

err:
	if (_in_str) free(_in_str);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_size(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_size_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_size_t* ms = SGX_CAST(ms_ecall_pointer_size_t*, pms);
	ms_ecall_pointer_size_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_size_t), ms, sizeof(ms_ecall_pointer_size_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	void* _tmp_ptr = __in_ms.ms_ptr;
	size_t _tmp_len = __in_ms.ms_len;
	size_t _len_ptr = _tmp_len;
	void* _in_ptr = NULL;

	CHECK_UNIQUE_POINTER(_tmp_ptr, _len_ptr);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_ptr != NULL && _len_ptr != 0) {
		_in_ptr = (void*)malloc(_len_ptr);
		if (_in_ptr == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_ptr, _len_ptr, _tmp_ptr, _len_ptr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_pointer_size(_in_ptr, _tmp_len);
	if (_in_ptr) {
		if (memcpy_verw_s(_tmp_ptr, _len_ptr, _in_ptr, _len_ptr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_ptr) free(_in_ptr);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_count(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_count_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_count_t* ms = SGX_CAST(ms_ecall_pointer_count_t*, pms);
	ms_ecall_pointer_count_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_count_t), ms, sizeof(ms_ecall_pointer_count_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_arr = __in_ms.ms_arr;
	size_t _tmp_cnt = __in_ms.ms_cnt;
	size_t _len_arr = _tmp_cnt * sizeof(int);
	int* _in_arr = NULL;

	if (sizeof(*_tmp_arr) != 0 &&
		(size_t)_tmp_cnt > (SIZE_MAX / sizeof(*_tmp_arr))) {
		return SGX_ERROR_INVALID_PARAMETER;
	}

	CHECK_UNIQUE_POINTER(_tmp_arr, _len_arr);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_arr != NULL && _len_arr != 0) {
		if ( _len_arr % sizeof(*_tmp_arr) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_arr = (int*)malloc(_len_arr);
		if (_in_arr == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_arr, _len_arr, _tmp_arr, _len_arr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_pointer_count(_in_arr, _tmp_cnt);
	if (_in_arr) {
		if (memcpy_verw_s(_tmp_arr, _len_arr, _in_arr, _len_arr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_arr) free(_in_arr);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_pointer_isptr_readonly(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_pointer_isptr_readonly_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_pointer_isptr_readonly_t* ms = SGX_CAST(ms_ecall_pointer_isptr_readonly_t*, pms);
	ms_ecall_pointer_isptr_readonly_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_pointer_isptr_readonly_t), ms, sizeof(ms_ecall_pointer_isptr_readonly_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	buffer_t _tmp_buf = __in_ms.ms_buf;
	size_t _tmp_len = __in_ms.ms_len;
	size_t _len_buf = _tmp_len;
	buffer_t _in_buf = NULL;

	CHECK_UNIQUE_POINTER(_tmp_buf, _len_buf);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_buf != NULL && _len_buf != 0) {
		_in_buf = (buffer_t)malloc(_len_buf);
		if (_in_buf == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s((void*)_in_buf, _len_buf, _tmp_buf, _len_buf)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_pointer_isptr_readonly(_in_buf, _tmp_len);

err:
	if (_in_buf) free((void*)_in_buf);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ocall_pointer_attr(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ocall_pointer_attr();
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_array_user_check(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_array_user_check_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_array_user_check_t* ms = SGX_CAST(ms_ecall_array_user_check_t*, pms);
	ms_ecall_array_user_check_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_array_user_check_t), ms, sizeof(ms_ecall_array_user_check_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_arr = __in_ms.ms_arr;


	ecall_array_user_check(_tmp_arr);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_array_in(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_array_in_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_array_in_t* ms = SGX_CAST(ms_ecall_array_in_t*, pms);
	ms_ecall_array_in_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_array_in_t), ms, sizeof(ms_ecall_array_in_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_arr = __in_ms.ms_arr;
	size_t _len_arr = 4 * sizeof(int);
	int* _in_arr = NULL;

	CHECK_UNIQUE_POINTER(_tmp_arr, _len_arr);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_arr != NULL && _len_arr != 0) {
		if ( _len_arr % sizeof(*_tmp_arr) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_arr = (int*)malloc(_len_arr);
		if (_in_arr == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_arr, _len_arr, _tmp_arr, _len_arr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_array_in(_in_arr);

err:
	if (_in_arr) free(_in_arr);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_array_out(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_array_out_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_array_out_t* ms = SGX_CAST(ms_ecall_array_out_t*, pms);
	ms_ecall_array_out_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_array_out_t), ms, sizeof(ms_ecall_array_out_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_arr = __in_ms.ms_arr;
	size_t _len_arr = 4 * sizeof(int);
	int* _in_arr = NULL;

	CHECK_UNIQUE_POINTER(_tmp_arr, _len_arr);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_arr != NULL && _len_arr != 0) {
		if ( _len_arr % sizeof(*_tmp_arr) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_arr = (int*)malloc(_len_arr)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_arr, 0, _len_arr);
	}
	ecall_array_out(_in_arr);
	if (_in_arr) {
		if (memcpy_verw_s(_tmp_arr, _len_arr, _in_arr, _len_arr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_arr) free(_in_arr);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_array_in_out(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_array_in_out_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_array_in_out_t* ms = SGX_CAST(ms_ecall_array_in_out_t*, pms);
	ms_ecall_array_in_out_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_array_in_out_t), ms, sizeof(ms_ecall_array_in_out_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_arr = __in_ms.ms_arr;
	size_t _len_arr = 4 * sizeof(int);
	int* _in_arr = NULL;

	CHECK_UNIQUE_POINTER(_tmp_arr, _len_arr);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_arr != NULL && _len_arr != 0) {
		if ( _len_arr % sizeof(*_tmp_arr) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		_in_arr = (int*)malloc(_len_arr);
		if (_in_arr == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		if (memcpy_s(_in_arr, _len_arr, _tmp_arr, _len_arr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}

	}
	ecall_array_in_out(_in_arr);
	if (_in_arr) {
		if (memcpy_verw_s(_tmp_arr, _len_arr, _in_arr, _len_arr)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_arr) free(_in_arr);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_array_isary(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_array_isary_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_array_isary_t* ms = SGX_CAST(ms_ecall_array_isary_t*, pms);
	ms_ecall_array_isary_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_array_isary_t), ms, sizeof(ms_ecall_array_isary_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;


	ecall_array_isary((__in_ms.ms_arr != NULL) ? (*__in_ms.ms_arr) : NULL);


	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_function_public(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ecall_function_public();
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_function_private(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_function_private_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_function_private_t* ms = SGX_CAST(ms_ecall_function_private_t*, pms);
	ms_ecall_function_private_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_function_private_t), ms, sizeof(ms_ecall_function_private_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int _in_retval;


	_in_retval = ecall_function_private();
	if (memcpy_verw_s(&ms->ms_retval, sizeof(ms->ms_retval), &_in_retval, sizeof(_in_retval))) {
		status = SGX_ERROR_UNEXPECTED;
		goto err;
	}

err:
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_malloc_free(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ecall_malloc_free();
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_sgx_cpuid(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_sgx_cpuid_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_sgx_cpuid_t* ms = SGX_CAST(ms_ecall_sgx_cpuid_t*, pms);
	ms_ecall_sgx_cpuid_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_sgx_cpuid_t), ms, sizeof(ms_ecall_sgx_cpuid_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	int* _tmp_cpuinfo = __in_ms.ms_cpuinfo;
	size_t _len_cpuinfo = 4 * sizeof(int);
	int* _in_cpuinfo = NULL;

	CHECK_UNIQUE_POINTER(_tmp_cpuinfo, _len_cpuinfo);

	//
	// fence after pointer checks
	//
	sgx_lfence();

	if (_tmp_cpuinfo != NULL && _len_cpuinfo != 0) {
		if ( _len_cpuinfo % sizeof(*_tmp_cpuinfo) != 0)
		{
			status = SGX_ERROR_INVALID_PARAMETER;
			goto err;
		}
		if ((_in_cpuinfo = (int*)malloc(_len_cpuinfo)) == NULL) {
			status = SGX_ERROR_OUT_OF_MEMORY;
			goto err;
		}

		memset((void*)_in_cpuinfo, 0, _len_cpuinfo);
	}
	ecall_sgx_cpuid(_in_cpuinfo, __in_ms.ms_leaf);
	if (_in_cpuinfo) {
		if (memcpy_verw_s(_tmp_cpuinfo, _len_cpuinfo, _in_cpuinfo, _len_cpuinfo)) {
			status = SGX_ERROR_UNEXPECTED;
			goto err;
		}
	}

err:
	if (_in_cpuinfo) free(_in_cpuinfo);
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_exception(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ecall_exception();
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_map(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ecall_map();
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_increase_counter(void* pms)
{
	CHECK_REF_POINTER(pms, sizeof(ms_ecall_increase_counter_t));
	//
	// fence after pointer checks
	//
	sgx_lfence();
	ms_ecall_increase_counter_t* ms = SGX_CAST(ms_ecall_increase_counter_t*, pms);
	ms_ecall_increase_counter_t __in_ms;
	if (memcpy_s(&__in_ms, sizeof(ms_ecall_increase_counter_t), ms, sizeof(ms_ecall_increase_counter_t))) {
		return SGX_ERROR_UNEXPECTED;
	}
	sgx_status_t status = SGX_SUCCESS;
	size_t _in_retval;


	_in_retval = ecall_increase_counter();
	if (memcpy_verw_s(&ms->ms_retval, sizeof(ms->ms_retval), &_in_retval, sizeof(_in_retval))) {
		status = SGX_ERROR_UNEXPECTED;
		goto err;
	}

err:
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_producer(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ecall_producer();
	return status;
}

static sgx_status_t SGX_CDECL sgx_ecall_consumer(void* pms)
{
	sgx_status_t status = SGX_SUCCESS;
	if (pms != NULL) return SGX_ERROR_INVALID_PARAMETER;
	ecall_consumer();
	return status;
}

SGX_EXTERNC const struct {
	size_t nr_ecall;
	struct {void* ecall_addr; uint8_t is_priv; uint8_t is_switchless;} ecall_table[38];
} g_ecall_table = {
	38,
	{
		{(void*)(uintptr_t)sgx_ecall_add, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_create_wallet, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_deal_transaction, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_decrypt_transaction_data, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_encrypt_transaction, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_transaction_warning, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_char, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_int, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_float, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_double, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_size_t, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_wchar_t, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_struct, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_type_enum_union, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_user_check, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_in, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_out, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_in_out, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_string, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_string_const, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_size, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_count, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_pointer_isptr_readonly, 0, 0},
		{(void*)(uintptr_t)sgx_ocall_pointer_attr, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_array_user_check, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_array_in, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_array_out, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_array_in_out, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_array_isary, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_function_public, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_function_private, 1, 0},
		{(void*)(uintptr_t)sgx_ecall_malloc_free, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_sgx_cpuid, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_exception, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_map, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_increase_counter, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_producer, 0, 0},
		{(void*)(uintptr_t)sgx_ecall_consumer, 0, 0},
	}
};

SGX_EXTERNC const struct {
	size_t nr_ocall;
	uint8_t entry_table[14][38];
} g_dyn_entry_table = {
	14,
	{
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
	}
};


sgx_status_t SGX_CDECL ocall_print_string(const char* str)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_str = str ? strlen(str) + 1 : 0;

	ms_ocall_print_string_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_print_string_t);
	void *__tmp = NULL;


	CHECK_ENCLAVE_POINTER(str, _len_str);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (str != NULL) ? _len_str : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_print_string_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_print_string_t));
	ocalloc_size -= sizeof(ms_ocall_print_string_t);

	if (str != NULL) {
		if (memcpy_verw_s(&ms->ms_str, sizeof(const char*), &__tmp, sizeof(const char*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_str % sizeof(*str) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, str, _len_str)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_str);
		ocalloc_size -= _len_str;
	} else {
		ms->ms_str = NULL;
	}

	status = sgx_ocall(0, ms);

	if (status == SGX_SUCCESS) {
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_file_exists(const char* filename, int* exists)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_filename = filename ? strlen(filename) + 1 : 0;
	size_t _len_exists = sizeof(int);

	ms_ocall_file_exists_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_file_exists_t);
	void *__tmp = NULL;

	void *__tmp_exists = NULL;

	CHECK_ENCLAVE_POINTER(filename, _len_filename);
	CHECK_ENCLAVE_POINTER(exists, _len_exists);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (filename != NULL) ? _len_filename : 0))
		return SGX_ERROR_INVALID_PARAMETER;
	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (exists != NULL) ? _len_exists : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_file_exists_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_file_exists_t));
	ocalloc_size -= sizeof(ms_ocall_file_exists_t);

	if (filename != NULL) {
		if (memcpy_verw_s(&ms->ms_filename, sizeof(const char*), &__tmp, sizeof(const char*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_filename % sizeof(*filename) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, filename, _len_filename)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_filename);
		ocalloc_size -= _len_filename;
	} else {
		ms->ms_filename = NULL;
	}

	if (exists != NULL) {
		if (memcpy_verw_s(&ms->ms_exists, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_exists = __tmp;
		if (_len_exists % sizeof(*exists) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		memset_verw(__tmp_exists, 0, _len_exists);
		__tmp = (void *)((size_t)__tmp + _len_exists);
		ocalloc_size -= _len_exists;
	} else {
		ms->ms_exists = NULL;
	}

	status = sgx_ocall(1, ms);

	if (status == SGX_SUCCESS) {
		if (exists) {
			if (memcpy_s((void*)exists, _len_exists, __tmp_exists, _len_exists)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_read_file(const char* filename, char* buffer, size_t buffer_size, int* ret)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_filename = filename ? strlen(filename) + 1 : 0;
	size_t _len_buffer = buffer_size;
	size_t _len_ret = sizeof(int);

	ms_ocall_read_file_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_read_file_t);
	void *__tmp = NULL;

	void *__tmp_buffer = NULL;
	void *__tmp_ret = NULL;

	CHECK_ENCLAVE_POINTER(filename, _len_filename);
	CHECK_ENCLAVE_POINTER(buffer, _len_buffer);
	CHECK_ENCLAVE_POINTER(ret, _len_ret);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (filename != NULL) ? _len_filename : 0))
		return SGX_ERROR_INVALID_PARAMETER;
	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (buffer != NULL) ? _len_buffer : 0))
		return SGX_ERROR_INVALID_PARAMETER;
	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (ret != NULL) ? _len_ret : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_read_file_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_read_file_t));
	ocalloc_size -= sizeof(ms_ocall_read_file_t);

	if (filename != NULL) {
		if (memcpy_verw_s(&ms->ms_filename, sizeof(const char*), &__tmp, sizeof(const char*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_filename % sizeof(*filename) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, filename, _len_filename)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_filename);
		ocalloc_size -= _len_filename;
	} else {
		ms->ms_filename = NULL;
	}

	if (buffer != NULL) {
		if (memcpy_verw_s(&ms->ms_buffer, sizeof(char*), &__tmp, sizeof(char*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_buffer = __tmp;
		if (_len_buffer % sizeof(*buffer) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		memset_verw(__tmp_buffer, 0, _len_buffer);
		__tmp = (void *)((size_t)__tmp + _len_buffer);
		ocalloc_size -= _len_buffer;
	} else {
		ms->ms_buffer = NULL;
	}

	if (memcpy_verw_s(&ms->ms_buffer_size, sizeof(ms->ms_buffer_size), &buffer_size, sizeof(buffer_size))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	if (ret != NULL) {
		if (memcpy_verw_s(&ms->ms_ret, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_ret = __tmp;
		if (_len_ret % sizeof(*ret) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		memset_verw(__tmp_ret, 0, _len_ret);
		__tmp = (void *)((size_t)__tmp + _len_ret);
		ocalloc_size -= _len_ret;
	} else {
		ms->ms_ret = NULL;
	}

	status = sgx_ocall(2, ms);

	if (status == SGX_SUCCESS) {
		if (buffer) {
			if (memcpy_s((void*)buffer, _len_buffer, __tmp_buffer, _len_buffer)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
		if (ret) {
			if (memcpy_s((void*)ret, _len_ret, __tmp_ret, _len_ret)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_write_file(const char* filename, const char* data, size_t data_size, int write_mode, int* ret)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_filename = filename ? strlen(filename) + 1 : 0;
	size_t _len_data = data_size;
	size_t _len_ret = sizeof(int);

	ms_ocall_write_file_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_write_file_t);
	void *__tmp = NULL;

	void *__tmp_ret = NULL;

	CHECK_ENCLAVE_POINTER(filename, _len_filename);
	CHECK_ENCLAVE_POINTER(data, _len_data);
	CHECK_ENCLAVE_POINTER(ret, _len_ret);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (filename != NULL) ? _len_filename : 0))
		return SGX_ERROR_INVALID_PARAMETER;
	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (data != NULL) ? _len_data : 0))
		return SGX_ERROR_INVALID_PARAMETER;
	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (ret != NULL) ? _len_ret : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_write_file_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_write_file_t));
	ocalloc_size -= sizeof(ms_ocall_write_file_t);

	if (filename != NULL) {
		if (memcpy_verw_s(&ms->ms_filename, sizeof(const char*), &__tmp, sizeof(const char*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_filename % sizeof(*filename) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, filename, _len_filename)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_filename);
		ocalloc_size -= _len_filename;
	} else {
		ms->ms_filename = NULL;
	}

	if (data != NULL) {
		if (memcpy_verw_s(&ms->ms_data, sizeof(const char*), &__tmp, sizeof(const char*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_data % sizeof(*data) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, data, _len_data)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_data);
		ocalloc_size -= _len_data;
	} else {
		ms->ms_data = NULL;
	}

	if (memcpy_verw_s(&ms->ms_data_size, sizeof(ms->ms_data_size), &data_size, sizeof(data_size))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	if (memcpy_verw_s(&ms->ms_write_mode, sizeof(ms->ms_write_mode), &write_mode, sizeof(write_mode))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	if (ret != NULL) {
		if (memcpy_verw_s(&ms->ms_ret, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_ret = __tmp;
		if (_len_ret % sizeof(*ret) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		memset_verw(__tmp_ret, 0, _len_ret);
		__tmp = (void *)((size_t)__tmp + _len_ret);
		ocalloc_size -= _len_ret;
	} else {
		ms->ms_ret = NULL;
	}

	status = sgx_ocall(3, ms);

	if (status == SGX_SUCCESS) {
		if (ret) {
			if (memcpy_s((void*)ret, _len_ret, __tmp_ret, _len_ret)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_pointer_user_check(int* val)
{
	sgx_status_t status = SGX_SUCCESS;

	ms_ocall_pointer_user_check_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_pointer_user_check_t);
	void *__tmp = NULL;


	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_pointer_user_check_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_pointer_user_check_t));
	ocalloc_size -= sizeof(ms_ocall_pointer_user_check_t);

	if (memcpy_verw_s(&ms->ms_val, sizeof(ms->ms_val), &val, sizeof(val))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	status = sgx_ocall(4, ms);

	if (status == SGX_SUCCESS) {
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_pointer_in(int* val)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_val = sizeof(int);

	ms_ocall_pointer_in_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_pointer_in_t);
	void *__tmp = NULL;


	CHECK_ENCLAVE_POINTER(val, _len_val);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (val != NULL) ? _len_val : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_pointer_in_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_pointer_in_t));
	ocalloc_size -= sizeof(ms_ocall_pointer_in_t);

	if (val != NULL) {
		if (memcpy_verw_s(&ms->ms_val, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_val % sizeof(*val) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, val, _len_val)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_val);
		ocalloc_size -= _len_val;
	} else {
		ms->ms_val = NULL;
	}

	status = sgx_ocall(5, ms);

	if (status == SGX_SUCCESS) {
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_pointer_out(int* val)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_val = sizeof(int);

	ms_ocall_pointer_out_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_pointer_out_t);
	void *__tmp = NULL;

	void *__tmp_val = NULL;

	CHECK_ENCLAVE_POINTER(val, _len_val);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (val != NULL) ? _len_val : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_pointer_out_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_pointer_out_t));
	ocalloc_size -= sizeof(ms_ocall_pointer_out_t);

	if (val != NULL) {
		if (memcpy_verw_s(&ms->ms_val, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_val = __tmp;
		if (_len_val % sizeof(*val) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		memset_verw(__tmp_val, 0, _len_val);
		__tmp = (void *)((size_t)__tmp + _len_val);
		ocalloc_size -= _len_val;
	} else {
		ms->ms_val = NULL;
	}

	status = sgx_ocall(6, ms);

	if (status == SGX_SUCCESS) {
		if (val) {
			if (memcpy_s((void*)val, _len_val, __tmp_val, _len_val)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_pointer_in_out(int* val)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_val = sizeof(int);

	ms_ocall_pointer_in_out_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_ocall_pointer_in_out_t);
	void *__tmp = NULL;

	void *__tmp_val = NULL;

	CHECK_ENCLAVE_POINTER(val, _len_val);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (val != NULL) ? _len_val : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_ocall_pointer_in_out_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_ocall_pointer_in_out_t));
	ocalloc_size -= sizeof(ms_ocall_pointer_in_out_t);

	if (val != NULL) {
		if (memcpy_verw_s(&ms->ms_val, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_val = __tmp;
		if (_len_val % sizeof(*val) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, val, _len_val)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_val);
		ocalloc_size -= _len_val;
	} else {
		ms->ms_val = NULL;
	}

	status = sgx_ocall(7, ms);

	if (status == SGX_SUCCESS) {
		if (val) {
			if (memcpy_s((void*)val, _len_val, __tmp_val, _len_val)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL ocall_function_allow(void)
{
	sgx_status_t status = SGX_SUCCESS;
	status = sgx_ocall(8, NULL);

	return status;
}
sgx_status_t SGX_CDECL sgx_oc_cpuidex(int cpuinfo[4], int leaf, int subleaf)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_cpuinfo = 4 * sizeof(int);

	ms_sgx_oc_cpuidex_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_sgx_oc_cpuidex_t);
	void *__tmp = NULL;

	void *__tmp_cpuinfo = NULL;

	CHECK_ENCLAVE_POINTER(cpuinfo, _len_cpuinfo);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (cpuinfo != NULL) ? _len_cpuinfo : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_sgx_oc_cpuidex_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_sgx_oc_cpuidex_t));
	ocalloc_size -= sizeof(ms_sgx_oc_cpuidex_t);

	if (cpuinfo != NULL) {
		if (memcpy_verw_s(&ms->ms_cpuinfo, sizeof(int*), &__tmp, sizeof(int*))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp_cpuinfo = __tmp;
		if (_len_cpuinfo % sizeof(*cpuinfo) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		memset_verw(__tmp_cpuinfo, 0, _len_cpuinfo);
		__tmp = (void *)((size_t)__tmp + _len_cpuinfo);
		ocalloc_size -= _len_cpuinfo;
	} else {
		ms->ms_cpuinfo = NULL;
	}

	if (memcpy_verw_s(&ms->ms_leaf, sizeof(ms->ms_leaf), &leaf, sizeof(leaf))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	if (memcpy_verw_s(&ms->ms_subleaf, sizeof(ms->ms_subleaf), &subleaf, sizeof(subleaf))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	status = sgx_ocall(9, ms);

	if (status == SGX_SUCCESS) {
		if (cpuinfo) {
			if (memcpy_s((void*)cpuinfo, _len_cpuinfo, __tmp_cpuinfo, _len_cpuinfo)) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL sgx_thread_wait_untrusted_event_ocall(int* retval, const void* self)
{
	sgx_status_t status = SGX_SUCCESS;

	ms_sgx_thread_wait_untrusted_event_ocall_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_sgx_thread_wait_untrusted_event_ocall_t);
	void *__tmp = NULL;


	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_sgx_thread_wait_untrusted_event_ocall_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_sgx_thread_wait_untrusted_event_ocall_t));
	ocalloc_size -= sizeof(ms_sgx_thread_wait_untrusted_event_ocall_t);

	if (memcpy_verw_s(&ms->ms_self, sizeof(ms->ms_self), &self, sizeof(self))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	status = sgx_ocall(10, ms);

	if (status == SGX_SUCCESS) {
		if (retval) {
			if (memcpy_s((void*)retval, sizeof(*retval), &ms->ms_retval, sizeof(ms->ms_retval))) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL sgx_thread_set_untrusted_event_ocall(int* retval, const void* waiter)
{
	sgx_status_t status = SGX_SUCCESS;

	ms_sgx_thread_set_untrusted_event_ocall_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_sgx_thread_set_untrusted_event_ocall_t);
	void *__tmp = NULL;


	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_sgx_thread_set_untrusted_event_ocall_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_sgx_thread_set_untrusted_event_ocall_t));
	ocalloc_size -= sizeof(ms_sgx_thread_set_untrusted_event_ocall_t);

	if (memcpy_verw_s(&ms->ms_waiter, sizeof(ms->ms_waiter), &waiter, sizeof(waiter))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	status = sgx_ocall(11, ms);

	if (status == SGX_SUCCESS) {
		if (retval) {
			if (memcpy_s((void*)retval, sizeof(*retval), &ms->ms_retval, sizeof(ms->ms_retval))) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL sgx_thread_setwait_untrusted_events_ocall(int* retval, const void* waiter, const void* self)
{
	sgx_status_t status = SGX_SUCCESS;

	ms_sgx_thread_setwait_untrusted_events_ocall_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_sgx_thread_setwait_untrusted_events_ocall_t);
	void *__tmp = NULL;


	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_sgx_thread_setwait_untrusted_events_ocall_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_sgx_thread_setwait_untrusted_events_ocall_t));
	ocalloc_size -= sizeof(ms_sgx_thread_setwait_untrusted_events_ocall_t);

	if (memcpy_verw_s(&ms->ms_waiter, sizeof(ms->ms_waiter), &waiter, sizeof(waiter))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	if (memcpy_verw_s(&ms->ms_self, sizeof(ms->ms_self), &self, sizeof(self))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	status = sgx_ocall(12, ms);

	if (status == SGX_SUCCESS) {
		if (retval) {
			if (memcpy_s((void*)retval, sizeof(*retval), &ms->ms_retval, sizeof(ms->ms_retval))) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

sgx_status_t SGX_CDECL sgx_thread_set_multiple_untrusted_events_ocall(int* retval, const void** waiters, size_t total)
{
	sgx_status_t status = SGX_SUCCESS;
	size_t _len_waiters = total * sizeof(void*);

	ms_sgx_thread_set_multiple_untrusted_events_ocall_t* ms = NULL;
	size_t ocalloc_size = sizeof(ms_sgx_thread_set_multiple_untrusted_events_ocall_t);
	void *__tmp = NULL;


	CHECK_ENCLAVE_POINTER(waiters, _len_waiters);

	if (ADD_ASSIGN_OVERFLOW(ocalloc_size, (waiters != NULL) ? _len_waiters : 0))
		return SGX_ERROR_INVALID_PARAMETER;

	__tmp = sgx_ocalloc(ocalloc_size);
	if (__tmp == NULL) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}
	ms = (ms_sgx_thread_set_multiple_untrusted_events_ocall_t*)__tmp;
	__tmp = (void *)((size_t)__tmp + sizeof(ms_sgx_thread_set_multiple_untrusted_events_ocall_t));
	ocalloc_size -= sizeof(ms_sgx_thread_set_multiple_untrusted_events_ocall_t);

	if (waiters != NULL) {
		if (memcpy_verw_s(&ms->ms_waiters, sizeof(const void**), &__tmp, sizeof(const void**))) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		if (_len_waiters % sizeof(*waiters) != 0) {
			sgx_ocfree();
			return SGX_ERROR_INVALID_PARAMETER;
		}
		if (memcpy_verw_s(__tmp, ocalloc_size, waiters, _len_waiters)) {
			sgx_ocfree();
			return SGX_ERROR_UNEXPECTED;
		}
		__tmp = (void *)((size_t)__tmp + _len_waiters);
		ocalloc_size -= _len_waiters;
	} else {
		ms->ms_waiters = NULL;
	}

	if (memcpy_verw_s(&ms->ms_total, sizeof(ms->ms_total), &total, sizeof(total))) {
		sgx_ocfree();
		return SGX_ERROR_UNEXPECTED;
	}

	status = sgx_ocall(13, ms);

	if (status == SGX_SUCCESS) {
		if (retval) {
			if (memcpy_s((void*)retval, sizeof(*retval), &ms->ms_retval, sizeof(ms->ms_retval))) {
				sgx_ocfree();
				return SGX_ERROR_UNEXPECTED;
			}
		}
	}
	sgx_ocfree();
	return status;
}

